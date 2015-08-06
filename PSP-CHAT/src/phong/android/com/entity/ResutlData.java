package phong.android.com.entity;

import java.io.Serializable;

public class ResutlData implements Serializable {
	private static final long serialVersionUID = 1L;
	private String resultData;

	public ResutlData() {

	}
	public ResutlData(String resultData) {
		super();
		this.resultData = resultData;
	}

	public String getResult() {
		return resultData;
	}

	public void setResult(String resultData) {
		this.resultData = resultData;
	}


}
