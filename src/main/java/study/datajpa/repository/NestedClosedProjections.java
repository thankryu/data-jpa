package study.datajpa.repository;

/**
 * 프로젝션 중첩구조
 */
public interface NestedClosedProjections {

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }
}
