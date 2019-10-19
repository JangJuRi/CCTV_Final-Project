package com.example.cctv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Newsform extends AppCompatActivity implements View.OnClickListener{

    ArrayList<News> News_list = new ArrayList<News>();
    ListView listView;
    ImageButton NewsRe;
    LinearLayout NewsBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsform);

        listView = (ListView) findViewById(R.id.listview);
        ImageButton NewsRe = (ImageButton) findViewById(R.id.News_Re);
        NewsRe.setOnClickListener(this);

        NewsBar = (LinearLayout)findViewById(R.id.newsBar);

        int status = NetworkStatus.getConnectivityStatus(this);
        if(status == NetworkStatus.TYPE_MOBILE){
            new ReceiveNews().execute();
        }else if (status == NetworkStatus.TYPE_WIFI){
            new ReceiveNews().execute();
        }else {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("알림");
            alertDialog.setMessage("인터넷을 연결해주세요");
            alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alertDialog.show();
        }

        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));
            }
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }


        new ReceiveNews().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.News_Re:
                new ReceiveNews().execute();
                Toast.makeText(this, "뉴스 새로고침 완료!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class ReceiveNews extends AsyncTask<URL, Integer, Long> {

        protected Long doInBackground(URL... urls) {

            String url = "http://newssearch.naver.com/search.naver?where=rss&query=%EB%B2%94%EC%A3%84&field=0&nx_search_query=&nx_and_query=&nx_sub_query=&nx_search_hlquery=&is_dts=0";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();
                parseXML(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                return;
            }
            int totalHeight = 0;
            for (int i = 0; i < 14; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0,0);
                totalHeight += listItem.getMeasuredHeight();
                totalHeight+=NewsBar.getHeight();
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }

        protected void onPostExecute(Long result) {
//            String data = "";
//
//            for(int i=0; i<News_list.size(); i++) {
//                data += "제목 : "+News_list.get(i).getTitle() + "\n" +
//                        "내용 : "+News_list.get(i).getDescription() + "\n" +
//                        "날짜 : "+News_list.get(i).getPubDate() + "\n" +
//                        "신문사 : "+News_list.get(i).getAuthor() + "\n" +
//                        "카테고리 : "+News_list.get(i).getCategory() + "\n\n\n";
//            }
//
//            textView_News.setText(data);
            final MyAdapter myAdapter = new MyAdapter(getApplication(),News_list);
            listView.setAdapter(myAdapter);
            setListViewHeightBasedOnChildren(listView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String link_url = myAdapter.getItem(position).getLink();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link_url));
                    startActivity(intent);
                }
            });
        }

        void parseXML(String xml) {
            try {
                String tagName = "";
                boolean onTitle = false;
                boolean onDescription = false;
                boolean onPubDate = false;
                boolean onAuthor = false;
                boolean onCategory = false;
                boolean onLink = false;
                boolean onEnd = false;
                boolean isItemTag1 = false;
                int i = 0;

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();

                parser.setInput(new StringReader(xml));

                int eventType = parser.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        tagName = parser.getName();
                        if (tagName.equals("item")) {
                            News_list.add(new News());
                            onEnd = false;
                            isItemTag1 = true;
                        }
                    } else if (eventType == XmlPullParser.TEXT && isItemTag1) {
                        if (tagName.equals("title") && !onTitle) {
                            News_list.get(i).setTitle(parser.getText());
                            onTitle = true;
                        }
                        if (tagName.equals("description") && !onDescription) {
                            News_list.get(i).setDescription(parser.getText());
                            onDescription = true;
                        }
                        if (tagName.equals("pubDate") && !onPubDate) {
                            News_list.get(i).setPubDate(parser.getText());
                            onPubDate = true;
                        }
//                        if (tagName.equals("author") && !onAuthor) {
//                            News_list.get(i).setAuthor(parser.getText());
//                            onAuthor = true;
//                        }
                        if (tagName.equals("link") && !onLink) {
                            News_list.get(i).setLink(parser.getText());
                            onLink = true;
                        }
//                        if (tagName.equals("category") && !onCategory) {
//                            News_list.get(i).setCategory(parser.getText());
//                            onCategory = true;
//                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (tagName.equals("category") && onEnd == false) {
                            i++;
                            onTitle = false;
                            onDescription = false;
                            onPubDate = false;
                            onAuthor = false;
                            onCategory = false;
                            onLink = false;
                            isItemTag1 = false;
                            onEnd = true;
                        }
                    }

                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}