package com.quadcoder.coinpet.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Post{
	
	public static final String STATUS_IDLE = "idle";	//노멀
	public static final String STATUS_ADOPT = "adopt";	//채택 완료
	public static final String STATUS_REPORT = "report";	//신고글
	public static final String STATUS_DELETE = "delete";
	
	public String _id;
	public String dashing;
	public String language;
	public int callCount;
	public String date;
	public String status;
	public String[] _reports;
	public ArrayList<Answer> _answers;
	
	
}
