Lotto 번호 생성 프로그램

개발 기간 : 하루

주요 기능 : 

1. 로또 번호 생성
2. 당첨 확인
3. 지난 회차 당첨 번호 조회 (현재 1-50회)
4. 빈출 번호 많이 나온 순서대로 조회 (현재 1~50회)
5. deeplink (param - 회차)로 해당 회차 1등 번호 조회

사용 기술 : 
Room, Retrofit2, ButterKnife

디자인 패턴 : mvc, mvp



*deeplink adb shell command :
adb shell am start -a android.intent.action.VIEW -d "myscheme://myhost?drwNo={param}" com.myapp.htpad.lotto
