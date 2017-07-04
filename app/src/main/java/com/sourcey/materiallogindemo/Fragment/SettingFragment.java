package com.sourcey.materiallogindemo.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sourcey.materiallogindemo.BoardFragment;
import com.sourcey.materiallogindemo.LoginActivity;
import com.sourcey.materiallogindemo.MainActivity;
import com.sourcey.materiallogindemo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    View rootView;
    ListView listView;

    public SettingFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // 아이디 - 변수 매칭
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        listView = (ListView) rootView.findViewById(R.id.setting_list);

        getActivity().overridePendingTransition(0, 0); // 화면 전환 효과 없애기

        // 리스트뷰 제목 어레이
        String[] values = new String[]{
                "내 정보",
                "이용 약관",
                "개인 정보 취급 방침",
                "버전 정보",
                "개발사 문의",
                "로그아웃",
        };

        // 리스트뷰 생성을 위한 어레이 어댑터
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // 리스트뷰에 어댑터 적용
        listView.setAdapter(adapter);

        // 어떤 아이템 클릭되었는지에 따라 다른 함수 실행
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // 클릭된 아이템 타이틀 얻어오기
                String title = ((TextView) view).getText().toString();

                if (title.matches("버전 정보")) {
                    Toast.makeText(rootView.getContext(), "Stless version 1.0", Toast.LENGTH_LONG).show();
                } else if (title.matches("개발사 문의")){
                    contact();
                } else if (title.matches("로그아웃")) {
                    logout();
                } else {
                    // BoardFragment로 이동 및 title 데이터 전송
                    Bundle bundle = new Bundle();
                    bundle.putString("title", title);
                    BoardFragment fragment = new BoardFragment();
                    fragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }

        });


        return rootView;
    }

    // 로그아웃 다이얼로그 띄우기
    public void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("로그아웃 하시겠습니까?"); // Sets title for your alertbox

        alertDialog.setMessage("앱을 사용하려면 로그인이 필요합니다."); // Message to be displayed on alertbox

        /* When positive (yes/ok) is clicked */
        alertDialog.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getActivity(), "성공적으로 로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
            }
        });

        /* When negative (No/cancel) button is clicked*/
        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    // 개발사 문의 다이얼로그 띄우기
    public void contact() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        alertDialog.setTitle("개발사 문의"); // Sets title for your alertbox

        alertDialog.setMessage("054-260-1273"); // Message to be displayed on alertbox

        /* When positive (yes/ok) is clicked */
        alertDialog.setPositiveButton("전화걸기", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:054-260-1273"));
                startActivity(intent);
            }
        });

        /* When negative (No/cancel) button is clicked*/
        alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

}
