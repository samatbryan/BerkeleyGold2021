//#pragma GCC optimize("Ofast")
//#pragma GCC target("avx")
//#undef LOCAL
/**
 * Solution for the problem Matching On Bipartite Graph on Yosupo. 
 * It prints every match as well as print the max matching.
 * */

#include <algorithm>

#include <array>

#include <bitset>

#include <cassert>

#include <complex>

#include <cstdio>

#include <cstring>

#include <iostream>

#include <map>

#include <numeric>

#include <queue>

#include <set>

#include <string>

#include <unordered_map>

#include <unordered_set>

#include <vector>

using namespace std;

using uint = unsigned int;
using ll = long long;
using ull = unsigned long long;
constexpr ll TEN(int n) { return (n == 0) ? 1 : 10 * TEN(n - 1); }
template <class T>
using V = vector<T>;
template <class T>
using VV = V<V<T>>;

struct Scanner
{
    FILE *fp = nullptr;
    char line[(1 << 15) + 1];
    size_t st = 0, ed = 0;
    void reread()
    {
        memmove(line, line + st, ed - st);
        ed -= st;
        st = 0;
        ed += fread(line + ed, 1, (1 << 15) - ed, fp);
        line[ed] = '\0';
    }
    bool succ()
    {
        while (true)
        {
            if (st == ed)
            {
                reread();
                if (st == ed)
                    return false;
            }
            while (st != ed && isspace(line[st]))
                st++;
            if (st != ed)
                break;
        }
        if (ed - st <= 50)
            reread();
        return true;
    }
    template <class T, enable_if_t<is_same<T, string>::value, int> = 0>
    bool read_single(T &ref)
    {
        if (!succ())
            return false;
        while (true)
        {
            size_t sz = 0;
            while (st + sz < ed && !isspace(line[st + sz]))
                sz++;
            ref.append(line + st, sz);
            st += sz;
            if (!sz || st != ed)
                break;
            reread();
        }
        return true;
    }
    template <class T, enable_if_t<is_integral<T>::value, int> = 0>
    bool read_single(T &ref)
    {
        if (!succ())
            return false;
        bool neg = false;
        if (line[st] == '-')
        {
            neg = true;
            st++;
        }
        ref = T(0);
        while (isdigit(line[st]))
        {
            ref = 10 * ref + (line[st++] - '0');
        }
        if (neg)
            ref = -ref;
        return true;
    }
    template <class T>
    bool read_single(V<T> &ref)
    {
        for (auto &d : ref)
        {
            if (!read_single(d))
                return false;
        }
        return true;
    }
    void read() {}
    template <class H, class... T>
    void read(H &h, T &... t)
    {
        bool f = read_single(h);
        assert(f);
        read(t...);
    }
    Scanner(FILE *_fp) : fp(_fp) {}
};

struct Printer
{
public:
    template <bool F = false>
    void write() {}
    template <bool F = false, class H, class... T>
    void write(const H &h, const T &... t)
    {
        if (F)
            write_single(' ');
        write_single(h);
        write<true>(t...);
    }
    template <class... T>
    void writeln(const T &... t)
    {
        write(t...);
        write_single('\n');
    }

    Printer(FILE *_fp) : fp(_fp) {}
    ~Printer() { flush(); }

private:
    static constexpr size_t SIZE = 1 << 15;
    FILE *fp;
    char line[SIZE], small[50];
    size_t pos = 0;
    void flush()
    {
        fwrite(line, 1, pos, fp);
        pos = 0;
    }
    void write_single(const char &val)
    {
        if (pos == SIZE)
            flush();
        line[pos++] = val;
    }
    template <class T, enable_if_t<is_integral<T>::value, int> = 0>
    void write_single(T val)
    {
        if (pos > (1 << 15) - 50)
            flush();
        if (val == 0)
        {
            write_single('0');
            return;
        }
        if (val < 0)
        {
            write_single('-');
            val = -val; // todo min
        }
        size_t len = 0;
        while (val)
        {
            small[len++] = char('0' + (val % 10));
            val /= 10;
        }
        reverse(small, small + len);
        memcpy(line + pos, small, len);
        pos += len;
    }
    void write_single(const string &s)
    {
        for (char c : s)
            write_single(c);
    }
    void write_single(const char *s)
    {
        size_t len = strlen(s);
        for (size_t i = 0; i < len; i++)
            write_single(s[i]);
    }
    template <class T>
    void write_single(const V<T> &val)
    {
        auto n = val.size();
        for (size_t i = 0; i < n; i++)
        {
            if (i)
                write_single(' ');
            write_single(val[i]);
        }
    }
};

Scanner sc = Scanner(stdin);
Printer pr = Printer(stdout);

struct BipartiteMaching
{
    int n, m;
    VV<int> g;
    V<int> lmt, rmt, dist;
    void add_edge(int l, int r)
    {
        assert(0 <= l && l < n);
        assert(0 <= r && r < m);
        g[l].push_back(r);
        if (lmt[l] == -1 && rmt[r] == -1)
        {
            lmt[l] = r;
            rmt[r] = l;
        }
    }
    BipartiteMaching(int _n, int _m) : n(_n), m(_m), g(n), lmt(n, -1), rmt(m, -1), dist(n) {}

    void bfs()
    {
        int trg_dist = TEN(9);
        fill(dist.begin(), dist.end(), TEN(9));
        queue<int> que;
        for (int i = 0; i < n; i++)
        {
            if (lmt[i] == -1)
            {
                dist[i] = 0;
                que.push(i);
            }
        }
        while (!que.empty())
        {
            int p = que.front();
            que.pop();
            if (trg_dist - 1 <= dist[p])
                break;
            for (int q : g[p])
            {
                int next = rmt[q];
                if (next == -1)
                {
                    for (int i = 0; i < n; i++)
                    {
                        if (dist[p] < dist[i])
                            dist[i] = TEN(9);
                    }
                    return;
                }
                if (dist[next] == TEN(9))
                {
                    dist[next] = dist[p] + 1;
                    que.push(next);
                }
            }
        }
    }
    bool dfs(int p)
    {
        for (int q : g[p])
        {
            int next = rmt[q];
            if (next == -1 || (dist[p] + 1 == dist[next] && dfs(next)))
            {
                lmt[p] = q;
                rmt[q] = p;
                return true;
            }
        }
        return false;
    }

    void exec()
    {
        while (true)
        {
            bfs();
            bool update = false;
            for (int i = 0; i < n; i++)
            {
                if (lmt[i] == -1)
                {
                    if (dfs(i))
                        update = true;
                }
            }
            if (!update)
                break;
        }
    }
};

int main()
{
    int l, r, m;
    sc.read(l, r, m);
    BipartiteMaching bm(l, r);
    for (int i = 0; i < m; i++)
    {
        int x, y;
        sc.read(x, y);
        bm.add_edge(x, y);
    }
    bm.exec();
    using P = pair<int, int>;
    V<P> ans;
    for (int i = 0; i < l; i++)
    {
        if (bm.lmt[i] != -1)
        {
            ans.push_back({i, bm.lmt[i]});
        }
    }
    pr.writeln(ans.size());
    for (auto p : ans)
    {
        pr.writeln(p.first, p.second);
    }
    return 0;
}