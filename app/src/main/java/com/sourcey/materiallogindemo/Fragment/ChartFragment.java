package com.sourcey.materiallogindemo.Fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;

import com.sourcey.materiallogindemo.BinderData;
import com.sourcey.materiallogindemo.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {


    // XML node keys
    static final String KEY_TAG = "listdata"; // parent node
    static final String KEY_ID = "id";
    static final String KEY_DAY_OF_WEEK = "day_of_week";
    static final String KEY_DATE = "date";
    static final String KEY_AVERAGE = "average";
    static final String KEY_MAXIMUM = "maximum";
    static final String KEY_MINIMUM = "minimum";

    RadioButton week_btn;
    RadioButton month_btn;
    View rootView;
    ListView listView;
    BinderData adapter = null;
    List<HashMap<String, String>> dataCollection;
    boolean selected = true;

    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_chart, container, false);
        week_btn = (RadioButton) rootView.findViewById(R.id.week_btn);
        month_btn = (RadioButton) rootView.findViewById(R.id.month_btn);

        refreshListView();


//        // ArrayAdapter를 통해 LIST로 표시할 오브젝트를 지정한다.
//        // 여기서는 심플하게 그냥 String
//        // 레이아웃 android.R.layout.simple_list_item_1 는 안드로이드가 기본적으로 제공하는 간단한 아이템 레이아웃
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
//        // 아이템을 추가
//        adapter.add("화요일");
//        adapter.add("item2");
//        adapter.add("item3");
//        // ListView 가져오기
//        listView = (ListView) rootView.findViewById(R.id.listView);
//        // ListView에 각각의 아이템표시를 제어하는 Adapter를 설정
//        listView.setAdapter(adapter);


        week_btn.setOnClickListener(optionOnClickListener);
        month_btn.setOnClickListener(optionOnClickListener);
        week_btn.setChecked(true);

        return rootView;

    }


    void refreshListView(){
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document week_doc = docBuilder.parse(getActivity().getAssets().open("weekdata.xml"));
            Document month_doc = docBuilder.parse(getActivity().getAssets().open("monthdata.xml"));

            dataCollection = new ArrayList<HashMap<String, String>>();

            // normalize text representation
            week_doc.getDocumentElement().normalize();
            month_doc.getDocumentElement().normalize();

            NodeList dataList;
            if(selected){
                dataList = week_doc.getElementsByTagName("listdata");
            } else {
                dataList = month_doc.getElementsByTagName("listdata");
            }


            HashMap<String, String> map = null;

            for (int i = 0; i < dataList.getLength(); i++) {

                map = new HashMap<String, String>();

                Node firstDataNode = dataList.item(i);

                if (firstDataNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element firstDataElement = (Element) firstDataNode;
                    //-------
                    NodeList idList = firstDataElement.getElementsByTagName(KEY_ID);
                    Element firstIdElement = (Element) idList.item(0);
                    NodeList textIdList = firstIdElement.getChildNodes();
                    //--id
                    map.put(KEY_ID, ((Node) textIdList.item(0)).getNodeValue().trim());

                    //2.-------
                    NodeList weekList = firstDataElement.getElementsByTagName(KEY_DAY_OF_WEEK);
                    Element firstWeekElement = (Element) weekList.item(0);
                    NodeList textWeekList = firstWeekElement.getChildNodes();
                    //--city
                    map.put(KEY_DAY_OF_WEEK, ((Node) textWeekList.item(0)).getNodeValue().trim());

                    //3.-------
                    NodeList dateList = firstDataElement.getElementsByTagName(KEY_DATE);
                    Element firstDateElement = (Element) dateList.item(0);
                    NodeList textDateList = firstDateElement.getChildNodes();
                    //--city
                    map.put(KEY_DATE, ((Node) textDateList.item(0)).getNodeValue().trim());

                    //4.-------
                    NodeList averageList = firstDataElement.getElementsByTagName(KEY_AVERAGE);
                    Element firstAverageElement = (Element) averageList.item(0);
                    NodeList textAverageList = firstAverageElement.getChildNodes();
                    //--city
                    map.put(KEY_AVERAGE, ((Node) textAverageList.item(0)).getNodeValue().trim());

                    //5.-------
                    NodeList maximumList = firstDataElement.getElementsByTagName(KEY_MAXIMUM);
                    Element firstMaximumElement = (Element) maximumList.item(0);
                    NodeList textMaximumList = firstMaximumElement.getChildNodes();
                    //--city
                    map.put(KEY_MAXIMUM, ((Node) textMaximumList.item(0)).getNodeValue().trim());

                    //6.-------
                    NodeList minimumList = firstDataElement.getElementsByTagName(KEY_MINIMUM);
                    Element firstMinimumElement = (Element) minimumList.item(0);
                    NodeList textMinimumList = firstMinimumElement.getChildNodes();
                    //--city
                    map.put(KEY_MINIMUM, ((Node) textMinimumList.item(0)).getNodeValue().trim());

                    //Add to the Arraylist
                    dataCollection.add(map);
                }
            }

            BinderData bindingData = new BinderData(this, dataCollection);
            listView = (ListView) rootView.findViewById(R.id.listView);
            Log.i("BEFORE", "<<------------- Before SetAdapter-------------->>");
            listView.setAdapter(bindingData);
            Log.i("AFTER", "<<------------- After SetAdapter-------------->>");

        } catch (IOException ex) {
            Log.e("Error", ex.getMessage());
        } catch (Exception ex) {
            Log.e("Error", "Loading exception");
        }
    }

    RadioButton.OnClickListener optionOnClickListener
            = new RadioButton.OnClickListener() {
        public void onClick(View v) {
            if (week_btn.isChecked()) {
                week_btn.setTextColor(Color.BLACK);
                month_btn.setTextColor(Color.WHITE);
                selected = true;

            } else {
                week_btn.setTextColor(Color.WHITE);
                month_btn.setTextColor(Color.BLACK);
                selected = false;
            }
            refreshListView();
        }
    };

}
