#include <iostream>
#include <string>
#include <vector>
#include <deque>
#include <set>
#include <map>
#include <cmath>
#include <cstdlib>
#include <sstream>
#include <algorithm>
#include <numeric>
#include <functional>
#include <list>

#define loop(i,n) for ( int i = 0 , _n = (n); i < _n; ++i )
#define rep(i,a,b) for ( int i = (a) , _b = (b); i <= _b; ++i )
#define repd(i,a,b) for ( int i = (a) , _b = (b); i >= _b; --i )
#define loopit(p,c) for ( __typeof ( (c) . begin () ) p = (c) . begin (); p != (c) . end (); ++p )
#define MP make_pair
#define PB push_back
#define LL long long
#define infty 0x3F3F3F3F
#define eraseall(x,w) memset(x,w,sizeof x)
#define all(x) (x).begin(), (x).end()

#define sqr(x) (x)*(x)
#define two(i) (1<<(i))
#define getbit(i,n) (((n)>>(i))&1)
#define setbit(i,n,t) ((t)?((n)|(two(i))):((n)&~(two(i))))
#define subset(m,n) (((m)&(n))==(m))
#define F first
#define S second
#define pi M_PI
#define read(a) scanf ( " %d " , & a )
#define read2(a,b) scanf ( " %d %d " , & a , & b )
#define read3(a,b,c) scanf ( " %d %d %d " , & a , & b , & c )
#define read4(a,b,c,d) scanf ( " %d %d %d %d " , & a , & b , & c , & d )
#define out(a) debug && cout<<#a<<": "<<a<<endl;
#define out2(a,b) debug && cout<<"("<<#a<<": "<<a<<"),("<<#b<<": "<<b<<")"<<endl;
#define out3(a,b,c) debug && cout<<"("<<#a<<": "<<a<<"),("<<#b<<": "<<b<<"),("<<#c<<": "<<c<<")"<<endl;
#define out4(a,b,c,d) debug && cout<<"("<<#a<<": "<<a<<"),("<<#b<<": "<<b<<"),("<<#c<<": "<<c<<"),("<<#d<<": "<<d<<")"<<endl;
#define out1d(a,n) {debug && cout<<#a<<": "<<endl; loop(_i,n) debug && cout<<a[_i]<< " "; debug && cout<<endl;}
#define outp1d(a,n) {debug && cout<<#a<<": "<<endl; loop(_i,n) debug && cout<<"("<<a[_i].first<<","<<a[_i].second<<")"<<endl;}
#define out2d(a,sh,sc) {debug && cout<<#a<<": "<<endl; loop(_i,sh) { loop(_j,sc) debug && cout<<a[_i][_j]<<" "; debug && cout<<endl;} }
#define outstl(a) {debug && cout<<#a<<": "<<endl; loopit(it,a) debug && cout<<*it<<" "; debug && cout<<endl;}
#define outpstl(a) {debug && cout<<#a<<": "<<endl; loopit(it,a) debug && cout<<"("<<it->first<<","<<it->second<<")"<<endl;}
#define getRand(n) (((rand()<<16)+rand())%(n))
#define debug true
using namespace std;
typedef pair <int,int> pii;
typedef vector<int> vi;
typedef vector<pii> vii;

vector <pair<int,int> > g[2005];
int n, m, a[2005];

bool solve() {
	eraseall(a, 0);
	loop(i, m) if (g[i].size() == 1 && g[i][0].second == 1)
		a[g[i][0].first] = 1;

	loop(i, m) {
		bool ok = false;
		loop(j, g[i].size())
			if (a[g[i][j].first] == g[i][j].second) {
				ok = true;
				break;
			}
		if (!ok) return false;
	}
	return true;
}

bool stupid () {
	loop(mask, two(n)) {
		bool now = true;
		loop(i, m) {
			bool ok = false;
			loop(j, g[i].size()) {
				int at = g[i][j].first, need = g[i][j].second;
				if (getbit(at, mask) == need) {
					ok = true;
					break;
				}
			}
			if (!ok) {
				now = false;
				break;
			}
		}
		if (now) {
			loop(i, n) a[i] = getbit(i, mask);
			return true;
		}
	}
	return false;
}

int main() {
#ifndef ONLINE_JUDGE
    freopen("b.in", "r", stdin);
    freopen("out.txt", "w", stdout);
#endif

    int ntest; cin >> ntest;
    loop(test, ntest) {
    	cin >> n >> m;
    	loop(i, m) g[i].clear();
    	loop(i, m) {
    		int t; cin >> t;
    		loop(j, t) {
    			int x, y; cin >> x >> y;
    			g[i].push_back(MP(x - 1, y));
    		}
    	}

    	if (!stupid()) {
    		printf ("Case #%d: IMPOSSIBLE\n", test + 1);
    	}
    	else {
    		printf ("Case #%d:", test + 1);
    		loop(i, n) printf (" %d", a[i]);
    		printf ("\n");
    	}
    }

#ifndef ONLINE_JUDGE
    fclose(stdin);
    fclose(stdout);
#endif
    return 0;
}
