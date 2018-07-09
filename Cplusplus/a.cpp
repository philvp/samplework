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
#define infty 0x3F3F3F3F3F3F3F3FLL
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

LL f[10][two(8)];
LL solve (int a[], int b[], int n) {
	eraseall(f, infty);
	f[0][0] = 0;
	loop(i, n) loop(mask, two(n)) if (f[i][mask] != infty)
		loop(j, n) if (getbit(j, mask) == 0) {
			f[i+1][mask | two(j)] <?= f[i][mask] + b[i] * a[j];
		}
	return f[n][two(n) - 1];
}

LL stupid (int a[], int b[], int n) {
	LL res = 0;
	loop(i, n) res += (LL) a[i] * b[n - i - 1];
	return res;
}

int main() {
#ifndef ONLINE_JUDGE
    freopen("a2.in", "r", stdin);
    freopen("out.out", "w", stdout);
#endif

    int ntest; cin >> ntest;
    loop(test, ntest) {
    	int n; cin >> n;
    	int a[n], b[n];
    	loop(i, n) cin >> a[i];
    	loop(i, n) cin >> b[i];
    	sort(a, a + n);
    	sort(b, b + n);
    	cout << "Case #" << test + 1 << ": " << stupid(a, b, n) << endl;
    }

#ifndef ONLINE_JUDGE
    fclose(stdin);
    fclose(stdout);
#endif
    return 0;
}
