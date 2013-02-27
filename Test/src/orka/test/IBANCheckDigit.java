package orka.test;

public class IBANCheckDigit {

	private static final long serialVersionUID = -3600191725934382801L;

	/** Singleton IBAN Number Check Digit instance */

	private static final long MAX = 999999999;

	private static final long MODULUS = 97;

	/**
	 * Construct Check Digit routine for IBAN Numbers.
	 */
	public IBANCheckDigit() {
	}

	/**
	 * Validate the check digit for an the IBAN code.
	 * 
	 * @param code
	 *            The code to validate
	 * @return <code>true</code> if the check digit is valid, otherwise
	 *         <code>false</code>
	 */
	public boolean isValid(String code) {
		if (code == null || code.length() < 5) {
			return false;
		}
		try {
			int modulusResult = calculateModulus(code);
			return (modulusResult == 1);
		} catch (CheckDigitException ex) {
			return false;
		}
	}

	/**
	 * Calculate the <i>Check Digit</i> for an IBAN code.
	 * <p>
	 * <b>Note:</b> The check digit is the third and fourth characters and and
	 * should contain value "<code>00</code>".
	 * 
	 * @param code
	 *            The code to calculate the Check Digit for
	 * @return The calculated Check Digit
	 * @throws CheckDigitException
	 *             if an error occurs calculating the check digit for the
	 *             specified code
	 */
	public String calculate(String code) throws CheckDigitException {
		if (code == null || code.length() < 5) {
			throw new CheckDigitException("Invalid Code length="
					+ (code == null ? 0 : code.length()));
		}
		int modulusResult = calculateModulus(code);
		int charValue = (98 - modulusResult);
		String checkDigit = Integer.toString(charValue);
		return (charValue > 9 ? checkDigit : "0" + checkDigit);
	}

	/**
	 * Calculate the modulus for a code.
	 * 
	 * @param code
	 *            The code to calculate the modulus for.
	 * @return The modulus value
	 * @throws CheckDigitException
	 *             if an error occurs calculating the modulus for the specified
	 *             code
	 */
	private int calculateModulus(String code) throws CheckDigitException {
		// System.err.println("code: " + code);
		String reformattedCode = code.substring(4) + code.substring(0, 4);
		// System.err.println("reformattedCode: " + reformattedCode);
		long total = 0;
		for (int i = 0; i < reformattedCode.length(); i++) {
			int charValue = Character
					.getNumericValue(reformattedCode.charAt(i));
			if (charValue < 0 || charValue > 35) {
				throw new CheckDigitException("Invalid Character[" + i
						+ "] = '" + charValue + "'");
			}
			total = (charValue > 9 ? total * 100 : total * 10) + charValue;
			if (total > MAX) {
				total = (total % MODULUS);
			}
		}
		return (int) (total % MODULUS);
	}
}
