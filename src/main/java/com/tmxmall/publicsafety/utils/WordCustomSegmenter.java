package com.tmxmall.publicsafety.utils;

import java.util.List;

import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;


public class WordCustomSegmenter
{
	public static String segment(String text)
	{
		StringBuffer sb = new StringBuffer();
		List<Word> words = WordSegmenter.seg(text);
		if(words != null && words.size() > 0) {
			for (Word word : words) {
				sb.append(word.getText()).append(" ");
			}
		}
		return sb.toString();
	}
	
}
