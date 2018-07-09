import urllib2
import os
import re
from datetime import date
from datetime import timedelta

outputDir = r'temp/'
MAX_FAIL = 10

def date_iterator(start_date, end_date):
    '''
    start_date and end_date in form of arrays [year, month, day]
    returns inclusive span
    '''
    start_date = date(start_date[0], start_date[1], start_date[2])
    end_date = date(end_date[0], end_date[1], end_date[2])
    assert start_date < end_date, 'invalid date range'
    for i in range((end_date - start_date).days + 1):
        yield start_date + timedelta(i)

# couple of utility functions for writing current to log
# integer representation only down to days
# integer = date.year * 10000 + int.month * 100 + int.day
        
def int_to_date(i):
    day = int(i % 100);
    i = (i - day) / 100
    month = int(i % 100)
    i = (i - month) / 100
    year = int(i)
    return date(year, month, day)
    
def date_to_int(d):
    return d.year * 10000 + d.month * 100 + d.day

# output should be an integer
def crashdump(f, output):
    with open(f, 'w') as dump:
        dump.write(output)

# returns integer
def crashload(f):
    with open(f, 'r') as load:
        value = int(load.read().strip())
        load.close()
        return value

# utility function
def fetch(url):
    return urllib2.urlopen(url).read()

def remove_whitespace(data):
    p = re.compile(r'\s+')
    return p.sub(' ', data)

def remove_HTMLTags(data):
    htmlRegex = re.compile('<.*?>', re.DOTALL)
    return htmlRegex.sub(' ', data)

###############################################################################    
############################## End of function definition #####################
###############################################################################

class ContentExtractor:
    
    def feed(self, data):
        self.data = data
    
    def extract(self, start, end):
        regex = re.compile(start + '([\s\S]*?)' + end)
        instances = regex.findall(self.data)
        return instances
    
    def extract_multiple(self, delimiters):
        content = []
        for [start, end] in delimiters:
            regex = re.compile(start + '([\s\S]*?)' + end)
            try:
                instance = regex.findall(self.data)[0]
            except:
                instance = None
            content.append(instance)
        return content


class Storage():
    
    def __init__(self, address):
        self.address = address
    
    # key -> filename
    def store(self, key, value):
        filename = self.address + '\\' + key
        print 'Writing', filename
        filename = filename[:filename.rfind('\\')]
        if not os.path.exists(filename):
            os.makedirs(filename)
        with open(self.address + '\\' + key, 'w') as f:
            f.write(value)
    
    def retrieve(self, key):
        with open(self.address + '\\' + key, 'r') as f:
            return f.read()

# superclass to be extended by scraper for each site
class Scraper():
    
    # default
    logfile = 'log.txt'
    repository = 'repository'
    keyword = ''
    extractor = ContentExtractor()
    current = -1
    MAX = 100
    
    def load(self, log, repository, keyword=''):
        self.logfile = log
        self.repository = repository
        self.keyword = keyword
        self.storage = Storage(self.repository)
        self.current = int(crashload(self.logfile))
            
    def set_max(self, MAX):
        self.MAX = MAX
    
    def iter(self):
        '''
        the bulk of each scraper
        will be unique to each, iter should modify start variable
        will also be what stores data, remember to import storage
        '''
        pass
    
    def start(self):
        self.load(self.logfile, self.repository, self.keyword)
        while self.current <= self.MAX:
            self.iter()
            self.update()
    
    def parse(self, url):
        '''
        will be unique to each
        remember to import content_extractor
        '''
        pass
    
    def update(self):
        crashdump(self.logfile, str(self.current))

###############################################################################    
############################## Main class #####################################
###############################################################################

class EBEScraper(Scraper):
    def load(self, logfile, repository):
        self.logfile = logfile
        self.repository = repository
        if not os.path.exists(self.repository):
            os.makedirs(self.repository)
        if not os.path.exists(self.logfile):
            with open(self.logfile, 'w') as file:
                pass

    #http://www.eastbayexpress.com/gyrobase/ArticleArchives?category=1064131&page=1
    #http://www.eastbayexpress.com/gyrobase/ArticleArchives?category=1064131&page=2
    #...
    def iter(self):
        i = 0
        while True:
            i += 1
            link = 'http://www.eastbayexpress.com/gyrobase/ArticleArchives?category=1064131&page=' + str(i)
            fail = 0
            while fail < MAX_FAIL:
                try:
                    print '========================================================================'
                    print 'Fetching Page', i, link
                    archivePage = fetch(link)
                    print 'Finish fetching'
                    if archivePage.find('Oops! Couldn\'t find any articles that matched your search. Please try again.') != -1:
                        print 'Finish. No results on Page', i, link, 'Quit.'
                        return
                    break
                except:
                    print 'failed to fetch ' + link
                    fail += 1
            if fail >= MAX_FAIL:
                print 'Exit on failed'
            self.extractor.feed(archivePage)
            entries = self.extractor.extract('<li class="l0 storyItem">', '</li> <!-- end storyItem --')
            for entry in entries:
                entry = remove_whitespace(entry)
                regex = re.compile('<a href="([\s\S]*?)">([\s\S]*?)</a> </h4> <h5 class="subhead">([\s\S]*?)</h5> ')
                instance = regex.search(entry)
                href = instance.group(1).strip()
                title = instance.group(2).strip()
                week = instance.group(3).strip()
                author = re.search('<li class="l1 byline">by ([\s\S]*?)</li>', entry)
                if author:
                    author = author.group(1).strip()
                else:
                    author = 'UNKNOWN_AUTHOR'
                self.parseArticle(href, title, week, author)
    
    def parseArticle(self, link, title, week, author):
        print '========================'
        print title, week, author
        if self.inCache(title, week, author):
            print 'Already in cache. Exit.'
            return True
        fail = 0
        while fail < MAX_FAIL:
            try:
                print 'Fetching article', link
                article = fetch(link)
                print 'Finish fetching'
                break
            except:
                print 'failed to fetch ' + link
                fail += 1
        if fail >= MAX_FAIL:
            return False
        year, month, day = self.weekToDate(week);
        fileName = ''.join([year, month, day]) + '.txt'
        dir = self.repository + '/' + year
        if not os.path.exists(dir):
            os.makedirs(dir)
        print 'Writing ' + dir + '/' + fileName
        
        file = open(dir + '/' + fileName, 'w')
        file.write(title + '\n')
        self.extractor.feed(article);
        body = self.extractor.extract('"StoryLayout" class="SpanningFeature ContentDefault ">', '</div> <!-- end StoryLayout -->')[0]
        self.extractor.feed(body);
        paragraphs = self.extractor.extract('<p>', '</p>')
        for para in paragraphs:
            para = remove_whitespace(para)
            para = re.sub('&mdash;', '-', para)
            sign = re.search('<b>([\s\S]*?)</b>', para)
            if sign:
                frontPara = para[:sign.start()].strip()
                frontPara = remove_HTMLTags(frontPara);
                file.write(frontPara + '\n')
                file.write('*' + ' ' + sign.group(1).strip() + '\n')
                para = para[sign.end():].strip()
            para = remove_HTMLTags(para);
            file.write(para + '\n')
        file.close()
        self.writeLog(title, week, author)
        return True
 
    months = ['', 'January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
    def weekToDate(self, week):
        monthStr = re.search('the week of (\w+)', week)
        if monthStr:
            monthStr = re.search('the week of (\w+)', week).group(1).strip()
        else:
            monthStr = re.search('the Week of (\w+)', week).group(1).strip()
        month = str(self.months.index(monthStr))
        while len(month) < 2: month = '0' + month
        day = re.search(monthStr + ' (\d+)', week).group(1).strip()
        while len(day) < 2: day = '0' + day
        year = re.search(', (\d+).', week)
        if year:
            year = re.search(', (\d+)', week).group(1).strip()
        else:
            year = self.lastYear
        self.lastYear = year;
        return (year, month, day)
    
    def loadCache(self):
        self.cache = open(self.logfile, 'r').readlines()
        
    def inCache(self, title, week, author):
        cur = self.keyID(title, week, author) + '\n'
        return cur in self.cache
    
    def writeLog(self, title, week, author):
        cur = self.keyID(title, week, author) + '\n'
        with open(self.logfile, 'a') as f:
            f.write(cur)
        self.cache.append(cur)

    def keyID(self, title, week, author):
        return '@'.join([title, week, author]) 

def main():
    ebe = EBEScraper()
    ebe.load(outputDir + '/log.txt', outputDir)
    ebe.loadCache()
    ebe.iter()
    print 'Done from Scraper'

if __name__ == '__main__':
    main()
