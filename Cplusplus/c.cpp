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

template <class T> string tos (T what) {
    ostringstream out;
    out<<what;
    return out.str();
}

LL a = 3, b = 5;
LL nCr[35][35];
void init_nCr () {
	loop(n, 31) {
		nCr[n][0] = nCr[n][n] = 1;
		rep(r, 1, n-1) nCr[n][r] = nCr[n-1][r] + nCr[n-1][r-1];
	}
}

LL mu (LL &a, LL n) {
	if (n==0) return 1;
	LL t = mu(a, n/2);
	if (n % 2 == 0) return t * t;
	else return t * t * a;
}

string solve (int n) {
	LL x, y; x = y = 0;
	rep(i, 0, n)
		if (i % 2 == 0)
			x += nCr[n][i] * mu(b, i/2) * mu(a, n - i);
		else
			y += nCr[n][i] * mu(b, (i-1) / 2) * mu(a, n - i);
	double r = fmod(floor(y * sqrt(b)), 1000) + x % 1000;
	LL res = fmod(r, 1000);
	return tos(res);
}

int main() {
#ifndef ONLINE_JUDGE
    freopen("in.in", "r", stdin);
//    freopen("out.out", "w", stdout);
#endif

    init_nCr();
    int ntest; cin >> ntest;
    loop(test, ntest) {
    	int n; cin >> n;
    	string s = solve(n);
    	while (s.size() < 3) s = "0" + s;
    	printf ("Case #%d: %s\n", test + 1, s.c_str());
    }

#ifndef ONLINE_JUDGE
    fclose(stdin);
    fclose(stdout);
#endif
    return 0;
}
