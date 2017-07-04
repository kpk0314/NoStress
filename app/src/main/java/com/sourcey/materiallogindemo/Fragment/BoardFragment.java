package com.sourcey.materiallogindemo;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Isaac Kim on 2017-06-20.
 */

public class BoardFragment extends Fragment {

    View rootView;

    public BoardFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 아이디 - 변수 매칭
        rootView = inflater.inflate(R.layout.setting_board, container, false);
        TextView txtTitle = (TextView) rootView.findViewById(R.id.title_label);
        TextView txtContent = (TextView) rootView.findViewById(R.id.content_label);
        ListView listView = (ListView) rootView.findViewById(R.id.usrinfo_list);
        View endLine = (View) rootView.findViewById(R.id.end_line);

        getActivity().overridePendingTransition(0, 0); // 화면 전환 애니메이션 효과 없애기

        // SettingFragment로 부터 title 변수 받아 Title로 적용
        String title = getArguments().getString("title");
        txtTitle.setText(title);

        // Title에 따라 다른 내용 출력
        if (title.matches("내 정보")) {

            // 내 정보 리스트에 들어갈 스트링 값 생성
            String[] titles = new String[]{
                    "계정",
                    "이름",
                    "생년월일",
                    "성별",
                    "키",
                    "몸무게",
            };
            String[] contents = new String[]{
                    "ephiker@naver.com",
                    "서안드레",
                    "1991년 4월 10일",
                    "남성",
                    "170cm",
                    "62kg",
            };

            // 리스트뷰 어댑터 클래스를 이용하여 두개 이상의 리스트뷰의 한 아이템에 두개 이상의 텍스트뷰가 들어갈 수 있도록 설정
            ListViewAdapter lviewAdapter = new ListViewAdapter(getActivity(), titles, contents);
            listView.setAdapter(lviewAdapter);

            // 내 정보의 경우에만 endline 뷰가 visible하게 변하도록
            endLine.setVisibility(View.VISIBLE);

            // txtcontent의 경우 숨김
            txtContent.setVisibility(View.INVISIBLE);
        } else if (title.matches("이용 약관")) {
            txtContent.setText(
                    "제1조 (목적)\n" +
                            "본 'Stless 서비스 이용 약관' (이하 \"본 약관\"이라 합니다)은 이용자가 Stless에서 제공하는 각종 서비스 (이하 \"서비스\"라 합니다)를 이용함에 있어 \" Stless \"와 “이용자”의 권리 의무 및 책임 사항을 규정함을 목적으로 합니다.\n" +
                            "\n" +
                            "제2조 (정의)\n" +
                            "본 약관의 주요 용어는 아래와 같이 정의합니다.\n" +
                            "➀ \"Stless\"는 본 어플리케이션과, 본 어플리케이션의 개발팀을 말합니다. \n" +
                            "➁ \"서비스\"란 Stless 어플리케이션에서 제공하는 각종 서비스를 말합니다.\n" +
                            "➂ \"이용자”란 본 어플리케이션에 접속하여 이 약관에 따라 “Stless”가 제공하는 “서비스”를 받는 “회원”을 말합니다.\n" +
                            "➃ \"회원\"은 \" Stless\"에 개인 정보를 제공하여 회원 등록을 한 자로서, 회원 전용 \"서비스\"를 이용할 수 있는 자를 말합니다.\n" +
                            "이 약관에서 정하지 아니한 내용과 이 약관의 해석에 관하여는 전자상거래등에서의 소비자보호에관한법률, 약관의규제등에관한법률, 공정거래위원회가 정하는 전자상거래등에서의소비자보호지침 및 관계법령 또는 상관례에 따릅니다\n" +
                            "\n" +
                            "제3조 (서비스의 제공 및 변경)\n" +
                            "\"Stless\"는 \"이용자\"에게 아래와 같은 서비스를 제공합니다.\n" +
                            "➀ 스트레스 지수 계산\n" +
                            "➁ 스트레스 지수 로그 제공\n" +
                            "\n" +
                            "제4조 (서비스의 중단)\n" +
                            "\"Stless\"는 컴퓨터 등 전기통신설비의 보수점검 교체 및 고장, 통신의 두절, 전산 오류 등의 사유가 발생한 경우에는 \"서비스\"의 제공을 일시적으로 중단할 수 있습니다.\n" +
                            "\n" +
                            "제5조 (회원 가입)\n" +
                            "\"이용자\"는 \"Stless\"가 정한 가입 양식에 따라 회원 정보를 기입한 후, 이 약관에 동의한다는 의사 표시를 함으로써 회원 가입을 신청합니다.\n" +
                            "\n" +
                            "제6조 (회원의 탈퇴 및 회원자격 상실)\n" +
                            "\"회원\"은 \"Stless\"에 언제든지 자신의 회원 등록을 말소해 줄 것(회원 탈퇴)을 요청할 수 있으며 \"Stless\"는 위 요청을 받은 즉시 해당 \"이용자\"의 회원 등록 말소를 위한 절차를 밟습니다. 단, 절차가 진행되는 동안에는 기존 일정에 맞춰 예정된 \"서비스\"를 제공받을 수 있습니다.\n" +
                            "\n" +
                            "제7조 (\"이용자\"의 ID 및 비밀번호에 대한 의무)\n" +
                            "\"Stless\"가 관계법령, '개인정보취급방침'에 의해서 그 책임을 지는 경우를 제외하고, 자신의 ID와 비밀번호에 관한 관리책임은 각 \"이용자\"에게 있습니다.\n" +
                            "\"이용자\"는 자신의 ID 및 비밀번호를 제3자가 이용하게 해서는 안됩니다.\n" +
                            "\"이용자\"는 자신의 ID 및 비밀번호를 도난 당하거나 제3자가 사용하고 있음을 인지한 경우에는 바로 \"Stless\"에 통보하고 \"Stless\"의 안내가 있는 경우에는 그에 따라야 합니다..\n" +
                            "\"이용자\"가 이용자의 의무 조항을 어길 경우 \"Stless\"는 해당 \"이용자\"의 계정 및 서비스 이용에 제한을 가할 수 있습니다.\n" +
                            "\n" +
                            "제8조 (\"이용자\"의 의무)\n" +
                            "\"이용자\"는 다음 각 호의 행위를 하여서는 안됩니다\n" +
                            "➀ 회원 가입을 포함한 각종 신청 또는 변경 시 허위 내용을 등록하는 행위,\n" +
                            "➁ 타인의 정보 도용,\n" +
                            "➂ \"Stless\"가 게시한 정보의 변경.\n" +
                            "➃ \"Stless\"가 정한 정보 이외의 정보 (컴퓨터 프로그램 등) 등의 송신 또는 게시, \n" +
                            "➄ \"Stless\"및 기타 제 3자의 저작권 등 지적 재산권에 대한 침해,\n" +
                            "➅ \"Stless\"및 기타 제 3자의 명예를 손상시키거나 업무를 방해하는 행위, \n" +
                            "\n" +
                            "이 약관은 2017년 07월 5일부터 최초 시행되었습니다\n");
        } else if (title.matches("개인 정보 취급 방침")) {
            txtContent.setText(
                    "Stless는 고객의 개인정보보호를 소중하게 생각하고, 고객의 개인정보를 보호하기 위하여 항상 최선을 다해 노력하고 있습니다. Stless는 「정보통신망 이용촉진 및 정보보호 등에 관한 법률」을 비롯한 모든 개인정보보호 관련 법률규정 및 정보통신부가 제정한 「이동통신서비스제공자의 개인정보 보호지침」을 준수하고 있습니다.\n" +
                            "\n" +
                            "수집하는 개인정보의 항목 및 수집방법\n" +
                            "개인정보 수집에 대한 동의\n" +
                            "Stless는 고객님께서 회원가입 및 로그인 할 때, 그 행위가 개인정보취급방침 수집에 대해 동의하는 것이라고 알려드리는 메시지를 마련했으며, 회원가입 및 로그인의 행위를 하시면 개인정보 수집에 대해 동의한 것으로 봅니다.\n" +
                            "\n" +
                            "Stless가 \"서비스\" 이용과 관련하여 수집하는 개인정보의 범위는 아래와 같습니다.\n" +
                            "[사용자로부터 입력 받은 데이터]\n" +
                            "➀ 성명\n" +
                            "➁ 이메일주소\n" +
                            "➂ 비밀번호\n" +
                            "➃ 성별\n" +
                            "➄ 키, 몸무게\n" +
                            "➅ 생년월일\n" +
                            "\n" +
                            "[스트레스 분석을 위해 전달 받은 데이터]\n" +
                            "➀ Heart Rate\n" +
                            "➁ RR Interval\n" +
                            "➂ ACC_X, ACC_Y, ACC_Z\n" +
                            "➃ 스마트 밴드 착용 시간\n" +
                            "\n" +
                            "서비스 이용 또는 사업처리 과정에서 아래와 같은 정보들이 생성되어 수집될 수 있습니다.\n" +
                            "- 서비스이용기록, 접속로그, 쿠키, 접속IP정보, 결제기록, 이용정지기록\n" +
                            "\n" +
                            "개인 정보의 수집 방법은 아래와 같습니다.\n" +
                            "➀ 본 어플리케이션을 통한 회원가입\n" +
                            "➁ 스마트 밴드 연동\n" +
                            "\n" +
                            "개인정보의 수집 및 이용목적\n" +
                            "Stless는 수집 정보를 다음과 같은 목적을 위하여 사용합니다.\n" +
                            "➀ 사용자의 스트레스 지수 분석\n" +
                            "➁ 스트레스 분석 알고리즘의 정교화\n" +
                            "\n" +
                            "개인정보의 목적 외 사용 및 제3자에 대한 제공 및 공유\n" +
                            "Stless는 회원의 개인정보를 '개인정보의 수집항목 및 수집방법', '개인정보의 이용' 에서 고지한 범위 내에서 사용하며, 동 범위를 초과하여 이용하거나 타인 또는 타기업/기관에 제공하지 않습니다.\n" +
                            "\n" +
                            "개인정보의 공유 및 제공\n" +
                            "제공된 개인 정보는 당해 \"이용자\"의 동의 없이 목적 외의 이용이나 제 3자에게 제공할 수 없으며, 이에 대한 모든 책임은 Stless가 집니다. 다만, 다음의 경우에는 예외로 합니다.\n" +
                            "➀ 통계 작성, 학술 연구 또는 시장 조사를 위하여 필요한 경우로서 특정 개인을 식별할 수 없는 형태로 제공하는 경우.\n" +
                            "➁ 법률의 규정 또는 법률에 의하여 필요한 불가피한 사유가 있는 경우\n" +
                            "➂ 도용 방지를 위하여 본인 확인에 필요한 경우\n" +
                            "\n" +
                            "개인정보의 파기 절차 및 방법\n" +
                            "Stless가 회원님의 개인정보를 수집하는 경우, 회원님의 개인적인 요구가 있지 않은 이상 파기되지 않습니다. \n" +
                            "\n" +
                            "고지의 의무\n" +
                            "현 개인정보 취급(처리)방침은 2017년 7월 5일부터 적용됩니다.\n");
        }
//        } else if (title.matches("개발사 문의")) {
//            txtContent.setText("개발사 문의");
//
//        }

        // 뒤에 있는 프레그먼트로 터치가 전달되지 않도록 막는 함수
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;


    }


}
