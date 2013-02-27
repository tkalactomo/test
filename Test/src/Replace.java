import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;

public class Replace {

	public static void main(String[] args) {
		String putanjaFull = File.separator + "orka" + File.separator + "full"
				+ File.separator;

		File up = new File(putanjaFull);
		String[] datoteke = up.list();

		for (int i = 0; i < datoteke.length; i++) {
			try {
				String nazivDatoteke = datoteke[i];
				if (!nazivDatoteke.substring(0, 1).equals("o")) {
					continue;
				}

				FileInputStream fis = new FileInputStream(putanjaFull
						+ nazivDatoteke);
				InputStreamReader in = new InputStreamReader(fis);

				BufferedReader vage = new BufferedReader(in);
				String line = "";

				int br = 0;
				while ((line = vage.readLine()) != null) {
					int kilometri = Integer.parseInt(line.substring(207, 210)
							.trim());
					if (kilometri >= 51 && kilometri <= 55) {
						if (br == 0) {
							System.err.println("***********************"
									+ nazivDatoteke);
						}
						System.err.println(line);
						br++;
					}
				}

				if (br > 0) {
					System.err.println("broj za promijeniti: " + br);
				}

				vage.close();

			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}
	}

}
