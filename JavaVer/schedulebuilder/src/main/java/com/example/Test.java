package com.example;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.openqa.selenium.remote.http.HttpResponse;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Test {

    final static CloseableHttpClient httpClient = HttpClients.createDefault();
    public static void main( String[] args ) {

        try {
            HttpGet request = new HttpGet("https://boss.latech.edu/ia-bin/tsrvweb.cgi?&WID=W&WID&ConfigName=rcrssecthp1&ReqNum=1&TransactionSource=H&tserve_trans_config=rcrssecthp1.cfg&tserve_host_code=HostZero&tserve_tiphost_code=TipZero#top");
            CloseableHttpResponse getResponse = httpClient.execute(request);
            getResponse.close();

            String postUrl = "https://boss.latech.edu/ia-bin/tsrvweb.cgi";

            HttpPost postTerms = new HttpPost(postUrl);
            List<NameValuePair> urlTermParams = new ArrayList<>();
            urlTermParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
            urlTermParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
            urlTermParams.add(new BasicNameValuePair("tserve_trans_config", "RCRSSECTHP1.cfg"));
            urlTermParams.add(new BasicNameValuePair("tserve_tip_write", "%7C%7CWID%7CSID%7CPIN%7CTerm%7CSubject%7CCourseID%7CAppTerm%7CConfigName"));
            urlTermParams.add(new BasicNameValuePair("TransactionSource", "H"));
            urlTermParams.add(new BasicNameValuePair("Term", "20241"));
            postTerms.setEntity(new UrlEncodedFormEntity(urlTermParams));
            CloseableHttpResponse termResponse = httpClient.execute(postTerms);
            termResponse.close();

            HttpPost postSubject = new HttpPost(postUrl);
            List<NameValuePair> urlSubjectParams = new ArrayList<>();
            urlSubjectParams.add(new BasicNameValuePair("tserve_tip_read_destroy", ""));
            urlSubjectParams.add(new BasicNameValuePair("tserve_host_code", "HostZero"));
            urlSubjectParams.add(new BasicNameValuePair("tserve_tiphost_code", "TipZero"));
            urlSubjectParams.add(new BasicNameValuePair("tserve_trans_config", "rcrssecthp2.cfg"));
            urlSubjectParams.add(new BasicNameValuePair("tserve_tip_write", "%7C%7CWID%7CSID%7CPIN%7CTerm%7CSubject%7CCourseID%7CAppTerm%7CConfigName"));
            urlSubjectParams.add(new BasicNameValuePair("TransactionSource", "H"));
            urlSubjectParams.add(new BasicNameValuePair("Subject", "CSC"));
            urlSubjectParams.add(new BasicNameValuePair("ReqNum", "2"));
            postSubject.setEntity(new UrlEncodedFormEntity(urlSubjectParams));
            CloseableHttpResponse subjectResponse = httpClient.execute(postSubject);

            System.out.println(EntityUtils.toString(subjectResponse.getEntity()));
            subjectResponse.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
