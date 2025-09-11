package mes.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    // 작업비용(cost). 10~12 추천 (높을수록 느리지만 안전)
    private static final int COST = 10;

    public static String hash(String plain) {
        if (plain == null) throw new IllegalArgumentException("null password");
        return BCrypt.hashpw(plain, BCrypt.gensalt(COST));
    }

    public static boolean verify(String plain, String hashed) {
        if (plain == null || hashed == null) return false;
        // 해시가 아닌 평문이 DB에 남아있는 과도기 대비
        if (!hashed.startsWith("$2a$") && !hashed.startsWith("$2b$") && !hashed.startsWith("$2y$")) {
            return plain.equals(hashed);
        }
        return BCrypt.checkpw(plain, hashed);
    }

    /** 비용(cost) 낮거나 포맷이 legacy면 재해시 */
    public static boolean needsRehash(String hashed) {
        if (hashed == null) return true;
        if (!(hashed.startsWith("$2a$") || hashed.startsWith("$2b$") || hashed.startsWith("$2y$"))) return true;
        try {
            int cost = Integer.parseInt(hashed.substring(4, 6));
            return cost < COST;
        } catch (Exception e) { return true; }
    }
}
