package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    // String username 파라미터 명을 분석하니 주의해야 한다
    public UsernameOnlyDto(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
    }
}
