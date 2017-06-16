package com.complover116.q1r.core;

class Flags {
	public boolean hasFlag(int val, int flag) {
		return (val & flag) != 0;
	}
}
