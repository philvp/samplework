'''
Created on Feb 20, 2011

@author: Admin
'''
from Common import *
from cvxmod import *
from cvxmod.atoms import *
from cvxmod.sets import *
from l1regls import l1regls
import os
import sys

inputDir = 'data/ebeAquariumAge_archive'
outputDir = 'Result'
stopWordFile = 'NYTWStops.txt'
signs = ['@unknown', 'aries', 'taurus', 'gemini', 'cancer', 'leo', 'virgo', 'libra', 'scorpio', 'sagittarius', 'capricorn', 'aquarius', 'pisces']
fromTime = None
toTime = None

def loadStopWords(stopWordFile):
    lines = open(stopWordFile, 'r').read().split('\n')
    return [line.strip().split('\t')[1] for line in lines]

def main():
    stopWords = loadStopWords(stopWordFile)
    numArticles = zeroes(len(signs))
    wordOccurence = zeroes(len(signs))
    allWords = []
    for idSign in range(len(signs)):
        sign = signs[idSign]
        archiveDir = inputDir + '/' + sign
        paths = []
        for df in map(lambda x: archiveDir + '/' + x, os.listdir(archiveDir)):
            if os.path.isdir(df):
                paths.append(df)
            else:
                print df, ': not a dir'
        
        wordOccurence[idSign] = {}
        for path in paths:
            for f in os.listdir(path):
                if f == '.DS_Store':
                    continue
                if fromTime and f[:8] < fromTime:
                    continue
                if toTime and f[:8] > toTime:
                    continue
                article = readFullDoc(path, f)
                numArticles[idSign] = numArticles[idSign] + 1
                seenWords = []
                for word in article.split():
                    word = word.strip()
                    if word in stopWords or len(word) <= 1 or word in seenWords:
                        continue
                    if not(word in allWords):
                        allWords.append(word)
                    seenWords.append(word) 
                    if not word in wordOccurence[idSign]:
                        wordOccurence[idSign][word] = 0
                    wordOccurence[idSign][word] = wordOccurence[idSign][word] + 1
    
    allWords = sorted(allWords)
    
    wordID = {}
    for i in range(len(allWords)):
        wordID[allWords[i]] = i
        
    #Init Big Matrix
    A = zeroes(len(signs))
    for i in range(len(A)):
        A[i] = zeroes(len(allWords))
        
    for signID in range(len(A)):
        for word in wordOccurence[signID]:
            A[signID][wordID[word]] = wordOccurence[signID][word]
        A[signID].append(1) #ONE for Bias
        
# Transpose A        
    AT = zeroes(len(A[0]))
    for i in range(len(AT)):
        AT[i] = zeroes(len(A))

    for i in range(len(AT)):
        for j in range(len(AT[i])):
            AT[i][j] = A[j][i]
    
#    using CVXOPT
    Am = matrix(AT)
    distinctive = zeroes(len(A))
    for signID in range(len(A)):
        m = len(A)
        n = len(A[signID])
        b = [zeroes(len(A))]
        for i in range(len(A)):
            if i == signID:
                b[0][i] = 1
            else:
                b[0][i] = 0
        bm = matrix(b)
        x = l1regls(Am,bm)
        
        distinctive[signID] = {}
        for i in range(len(allWords)):
            if (abs(x[i]) > 1e-6):
                distinctive[signID][allWords[i]] = x[i]
                
# Output
    for signID in range(len(signs)):
        dir = outputDir + '/' + signs[signID]
        if not os.path.exists(dir):
            os.makedirs(dir)
        with open(dir + '/' + signs[signID] + '.txt', 'w') as file:
            dict = sortedValueDict(distinctive[signID])
            for item in dict:
                file.write(item[0] + ' ' + str(item[1]) + '\n')
                
    with open(outputDir + '/all.txt', 'w') as file:
        for signID in range(len(signs)):
            dict = sortedValueDict(distinctive[signID])
            file.write('=======' + '\n' + signs[signID] + '\n')
            for item in dict:
                file.write(item[0] + ' ' + str(item[1]) + '\n')

# Main running code        
if __name__ == '__main__':
    main()