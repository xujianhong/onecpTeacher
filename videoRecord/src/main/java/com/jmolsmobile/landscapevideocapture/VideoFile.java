/*
 *  Copyright 2016 Jeroen Mols
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jmolsmobile.landscapevideocapture;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoFile {

	private static final String	DIRECTORY_SEPARATOR	= "/";
	private static final String	DATE_FORMAT			= "yyyyMMdd_HHmmss";
	private static final String	DEFAULT_PREFIX		= "video_";
	private static final String	DEFAULT_EXTENSION	= ".mp4";

	private final String		mFilename;
	private Date				mDate;
	private int IsWater;
	private Context context;

	public VideoFile(String filename, int IsWater, Context context) {
		this.mFilename = filename;
		this.context = context;
		this.IsWater = IsWater;
	}

	public VideoFile(String filename, Date date, int IsWater, Context context) {
		this(filename,IsWater, context);
		this.mDate = date;
	}

	public String getFullPath() {
		return getFile().getAbsolutePath();
	}

	public File getFile() {
		final String filename = generateFilename();
		if (filename.contains(DIRECTORY_SEPARATOR)) return new File(filename);

		File path = null;
		if (IsWater == 1){
			path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/Camera");
		}else {
			path = context.getExternalFilesDir("Movies");
		}
		path.mkdirs();
		return new File(path, generateFilename());
	}

	private String generateFilename() {
		if (isValidFilename()) return mFilename;

		final String dateStamp = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(getDate());
		return DEFAULT_PREFIX + dateStamp + DEFAULT_EXTENSION;
	}

	private boolean isValidFilename() {
		if (mFilename == null) return false;
		if (mFilename.isEmpty()) return false;

		return true;
	}

	private Date getDate() {
		if (mDate == null) {
			mDate = new Date();
		}
		return mDate;
	}
}
