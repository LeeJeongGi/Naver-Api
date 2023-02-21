package com.naver.api;

import kr.go.localdata.client.DatasReceive;
import kr.go.localdata.client.ReceiveLocalDatas;
import org.junit.jupiter.api.Test;

public class test {

    @Test
    void test() {
        // 로컬 데이터 가져오기
        try {
            getData();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void getData() {

        // 요청 url : http://www.localdata.go.kr/platform/rest/TO0/openDataApi?authKey=p=abk6St04k0G4rkLVaZiuXtdOxMfLIU8sOfCOzVPes=
        // 인증키 : p=abk6St04k0G4rkLVaZiuXtdOxMfLIU8sOfCOzVPes=

        // 1.ReceiveLocalDatas 객체 생성
        ReceiveLocalDatas dr = new ReceiveLocalDatas();

        // 2. 파라미터 설정 (auth_key - 인증키, api_type - API유형, resultType - 결과형태, lastModTsBgn - 최종수정일자(시작일), lastModTsEnd - 최종수정일자(종료일), pageIndex - 페이지 번호 , pageSize - 페이지당 출력 개수)
        // 파라미터에 대한 상세 정보는 라이브러리다운 시 함께 제공되는 가이드 참조

        String auth_key = "p=abk6St04k0G4rkLVaZiuXtdOxMfLIU8sOfCOzVPes=";
        String api_type = "TO0";

        //결과형태 설정 - XML / JSON
        String resultType = "JSON";

        //receiveOpenMonthDatas() 호출 시 날짜 파라미터(YYYYMMDD 형식으로 입력)
        String lastModTsBgn = "";
        String lastModTsEnd = "";

        //호출 시 페이지 관련 변수( 1이상의 자연수로 입력 )
        int pageIndex = 1;
        int pageSize = 20;

        try {
            //receiveTotalCnt() - 제공 받을 데이터 총건수를 가져오는 메소드(M-receiveOpenMonthDatas 호출 시 사용, D-receiveOpenDayDatas 호출 시 사용)
            DatasReceive totalcnt = dr.receiveTotalCnt(api_type, auth_key, "M", null, null);
            int totalCnt = totalcnt.getReqTotalCnt();

            if (totalCnt == -1) {
                //서버 응답 확인
                System.out.println("Response code : " + totalcnt.getResultCode());
                System.out.println("Response Msg : " + totalcnt.getMsg());
            } else {
                //pageIndex 값을 1씩 늘려가면서 반복 호출 하기 위한 변수
                int forNum = (int) Math.ceil((double) totalCnt / pageSize);
                for (pageIndex = 1; pageIndex < forNum + 1; pageIndex++) {
                    //receiveOpenDayDatas() - 전일 변동분의 자료를 가져오는 메소드
                    //DatasReceive rld = dr.receiveOpenDayDatas(api_type,auth_key,resultType,pageIndex,pageSize);

                    //receiveOpenMonthDatas() - 현재 월 변동분의 자료를 가져오는 메소드 : 검색가능 날짜범위 - 전월 24일~ 현재 일자 2일전까지
                    DatasReceive rld = dr.receiveOpenMonthDatas(api_type, auth_key, resultType, lastModTsBgn, lastModTsEnd, pageIndex, pageSize);

                    if (rld.getResult() != 0) {

                        if (rld.getResult() == -1) {
                            System.out.println("### 서버 접속 실패 : 잠시 후 다시 이용바랍니다.");
                        }

                        //오류 결과 확인
                        System.out.println("Response code : " + rld.getResultCode());
                        System.out.println("Response Msg : " + rld.getMsg());
                    }

                    //서버 응답 확인
                    System.out.println("Response code : " + rld.getResultCode());
                    System.out.println("Response Msg : " + rld.getMsg());

                    //결과 확인
                    System.out.println("Response ResultData : " + rld.getResultData());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
