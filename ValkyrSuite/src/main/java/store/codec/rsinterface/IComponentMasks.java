/**
 * 
 */
package store.codec.rsinterface;

import lombok.AllArgsConstructor;

/**
 * Interface tool paolo 19/07/2019 #Shnek6969
 */
@AllArgsConstructor
public class IComponentMasks {

	public int settings, mask;

	public boolean method1879(boolean arg0) {
		if (((0x2eaa42 & settings) >> -1445214219 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean unlockedSlot(int slot, byte arg1) {
		if ((settings >> 1 + slot & 0x1 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1881(int arg0) {
		if (((0x55f65fb5 & settings) >> 1601172318 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1882(int arg0) {
		if (((settings & 0x1fa3c81f) >> -233371236 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1883(byte arg0) {
		if ((0x1 & settings >> 111606079 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1884(int arg0) {
		if ((0x1 & settings ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1885(byte arg0) {
		if ((0x1 & settings >> 1361505174 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public int method1887(int arg0) {
		return (settings & 0x1f873d) >> 1483551026;
	}

	public int method1888(byte arg0) {
		return 0x7f & settings >> -809958741;
	}

}
