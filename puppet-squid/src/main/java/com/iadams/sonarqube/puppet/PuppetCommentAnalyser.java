package com.iadams.sonarqube.puppet;

import org.sonar.squidbridge.CommentAnalyser;

/**
 * Created by iwarapter
 */
public class PuppetCommentAnalyser extends CommentAnalyser {

    @Override
    public boolean isBlank(String line) {
        for (int i = 0; i < line.length(); i++) {
            if (Character.isLetterOrDigit(line.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getContents(String comment) {
        if (comment.startsWith("#")) {
            return comment.substring(1);
        } else if (comment.startsWith("//")) {
            return comment.substring(2);
        } else if (comment.startsWith("/*")) {
            if (comment.endsWith("*/")) {
                return comment.substring(2, (int) comment.length() - 2);
            }
        } else {
            throw new IllegalArgumentException();
        }
        return null;
    }

}
