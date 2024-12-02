package com.flipkart.pibify.resources;

/**
 * This class is used for test
 * Author bageshwar.pn
 * Date 02/12/24
 */
public class SampleResponse {

    private long at;
    private String content;

    public SampleResponse() {
        // Jackson deserialization
    }

    public SampleResponse(long at, String content) {
        this.at = at;
        this.content = content;
    }

    public long getAt() {
        return at;
    }

    public void setAt(long at) {
        this.at = at;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
