package war;

import java.util.Date;

public class ConversionInfo {

	private Date conversionDate = new Date();
	private String sourceFileName;
	private String targetFileFormat;
	private String targetFilePath;

	public String getTargetFilePath() {
		return targetFilePath;
	}

	public void setTargetFilePath(String targetFilePath) {
		this.targetFilePath = targetFilePath;
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	public String getTargetFileFormat() {
		return targetFileFormat;
	}

	public void setTargetFileFormat(String targetFileFormat) {
		this.targetFileFormat = targetFileFormat;
	}

	/**
	 * @return the conversionDate
	 */
	public Date getConversionDate() {
		return this.conversionDate;
	}

	/**
	 * @param conversionDate the conversionDate to set
	 */
	public void setConversionDate(Date conversionDate) {
		this.conversionDate = conversionDate;
	}
}
