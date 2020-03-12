package com.samsung.solutions;

public class SWTPMiniCompiler {
}

/*

#include <iostream>

using namespace std;

const int MAX_REG = 32;
const int HAF_REG = MAX_REG / 2;
const int MAX_CMD = 4096;
const int MAX_STR = MAX_CMD * 2 + 1;
const int MAX_MOV = MAX_CMD * 500;

extern void run(const char* str);

// static int dummy1[...];

static int R[MAX_REG];  // Register : R0 ~ R31
static struct
{
	int c;  // cmd
	int i;  // Ri
	int j;  // Rj or Num
	int k;  // Rk
}   M[MAX_CMD];  // Memory
static int I = 0;  // Instruction Pointer

// static int dummy2[...];

static int testcase;
static char str[MAX_STR];
static int ans;

// static int dummy3[...];

static struct
{
	int c;  // count of cmd
	bool r;  // valid or invalid
	bool s;  // start command exist
	bool e;  // end command exist
}   rst;

// static int dummy4[...];

bool save(int cmd, int line)
{

	if (!rst.r)
		return false;
	++rst.c;
	if (line < 0 || MAX_CMD <= line)
		return (rst.r = false);
	if (line == 0)
	{
		if (cmd != (1 << 31))
			return (rst.r = false);
		M[0].c = 0;
		return (rst.s = true);
	}
	if (0 < line  && cmd == (1 << 31))
		return (rst.r = false);
	M[line].c = cmd >> 23;
	if (M[line].c == 128 || M[line].c == 64 || M[line].c == 32)  // (LOAD, Ri, Num) or (ADD, Ri, Num) or (SUB, Ri, Num)
	{
		M[line].i = cmd >> 18 & 31;
		M[line].j = cmd & 262143;
	}
	else if (M[line].c == 16 || M[line].c == 8)  // (ADD Ri, Rj) or (SUB Ri, Rj)
	{
		if (0 < (cmd & 8191))
			return (rst.r = false);
		M[line].i = cmd >> 18 & 31;
		M[line].j = cmd >> 13 & 31;
	}
	else if (M[line].c == 4)  // JMP Rk
	{
		if (0 < (cmd & 8380671))
			return (rst.r = false);
		M[line].k = cmd >> 8 & 31;
	}
	else if (M[line].c == 2)  // CJMP Ri, Rj, Rk
	{
		if (0 < (cmd & 255))
			return (rst.r = false);
		M[line].i = cmd >> 18 & 31;
		M[line].j = cmd >> 13 & 31;
		M[line].k = cmd >> 8 & 31;
	}
	else if (M[line].c == 1)  // END Ri
	{
		if (0 < (cmd & 262143))
			return (rst.r = false);
		M[line].i = cmd >> 18 & 31;
		rst.e = true;
	}
	else
	{
		return (rst.r = false);
	}
	return true;
}

static bool execute(void)
{
	if (!rst.r || !rst.s || !rst.e)
		return false;
	int mov = 0;
	while (++mov < MAX_MOV && ++I < MAX_CMD)
	{
		if (I < 1)
			break;
		switch (M[I].c)
		{
		case 1:  // END Ri
			cout <<"MOJA SUMA :"<<  R[M[I].i] << endl;
			if (ans == R[M[I].i])
				return true;
			return false;
		case 2:  // CJMP Ri, Rj, Rk
			if (R[M[I].i] == R[M[I].j])
			{
				I = R[M[I].k] - 1;
				if (I < 0 || MAX_CMD - 1 <= I)
					return false;
			}
			break;
		case 4:  // JMP Rk
			I = R[M[I].k] - 1;
			if (I < 0 || MAX_CMD - 1 <= I)
				return false;
			break;
		case 8:  // SUB Ri, Rj
			R[M[I].i] -= R[M[I].j];
			break;
		case 16:  // ADD Ri, Rj
			R[M[I].i] += R[M[I].j];
			break;
		case 32:  // SUB Ri, Num
			R[M[I].i] -= M[I].j;
			break;
		case 64:  // ADD Ri, Num
			R[M[I].i] += M[I].j;
			break;
		case 128:  // LOAD Ri, Num
			R[M[I].i] = M[I].j;
			break;
		}
	}
	return false;
}

static void init(void)
{
	for (int r = 0; r < HAF_REG; ++r)
		R[HAF_REG + r] = 0;  // Register : R[16] ~ R[31] = 0
	for (int c = 0; c < MAX_CMD; ++c)
		M[c].c = M[c].i = M[c].j = M[c].k = 0;  // Memory
	I = 0;  // instruction pointer initialize

	rst.c = 0;
	rst.r = true;
	rst.s = false;
	rst.e = false;
}

int main(void)
{
	freopen("sample_input.txt", "r", stdin);

	int total = 0;  // total count
	cin >> testcase;
	for (int t = 1; t <= testcase; ++t)
	{
		// input
		for (int r = 0; r < HAF_REG; ++r)
			cin >> R[r];
		cin >> str >> ans;

		// initialize
		init();

		// implement run() function
		run(str);

		// execute
		if (!execute())
			rst.c = 1000000;
		total += rst.c;

		// print result
		cout << "#" << t << " " << rst.c << endl;
	}

	// print total count
	cout << "total : " << total << endl;

	return 0;
}

#define MAX_CMD 4096
#define MAX_STR (MAX_CMD * 2 + 1)
#define SUM 16

extern bool save(int cmd, int line);

void run(const char* str)
{
}

5
0 0 0 6 0 0 0 0 0 0 0 0 0 0 0 0
R3*5+100
130
11 12 0 0 0 0 0 0 0 0 0 0 0 0 0 0
R0+R1
23
15 -60 20 -10 0 0 0 0 0 0 0 0 0 0 0 0
R0+R1-R2-R3
-55
10 10 10 10 10 10 10 10 10 10 10 10 10 10 10 10
R0*10+R1*11+R2*12+R3*13+R4*14+R5*15+R6*16+R7*17+R8*18+R9*19+R10*20+R11*21+R12*22+R13*23+R14*24+R15*25
2800
5 6 -7 0 0 0 0 0 0 0 0 0 0 0 0 0
R0+R1*200-R0+120000*5-500*100+130000+R0-R0+R2*200+170000
849800


 */