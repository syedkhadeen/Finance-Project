package app;

import java.util.StringTokenizer;

public class Strings {
    String string = "";
    String errorMsg = "";
    Error error;

    Strings(Error error) {
        this.error = error;
    }

    String quotizeJsonStringOld(String str) {
        String qStr = "";
        String qt = "\"";
        String kw = "", kv = "";
        String[] strParts = str.split(":");
        int len = 0;
        String errmsg;
        if (str == "") {
            errmsg = "No Json string was specified: " + str;
            error.setError(getClass().getName(), errmsg);
            return str;
        }

        //
        if (str.charAt(0) == '{') { // first colon term has not key value
            // all others chars between colon have key vaue then key word
            len = strParts[0].length();
            if (len == 0) {
                errmsg = "Invalid Json string detected: " + str;
                error.setError(getClass().getName(), errmsg);
                return str;
            }
            kw = "{" + qt + strParts[0].substring(1, len) + qt;
            qStr = kw + ":";
        } else {
            len = strParts[0].length();
            if (len == 0) {
                errmsg = "Invalid Json string detectes: " + str;
                error.setError(getClass().getName(), errmsg);
                return str;
            }
            kw = "{" + qt + strParts[0].substring(0, len) + qt;
            qStr = kw + ":";
        }

        // get sequences of key value key word
        // number key value key word pairs must be even
        // first term has no key value, last term has nio key word
        int noParts = strParts.length;
        // consider the next key value and key word pair term is key value and no key
        // word
        // separation between key value and key word depends on whether the next
        // character in strParts[odd] is [
        // if next character is [ then use ], as sepeator between key value and key word
        int np = 0;
        String[] kvKw;
        while (++np <= noParts - 2) { // each part in the loop has a key value then key word
            String strTerm = strParts[np].replaceAll("\"", "");
            if (strTerm.charAt(0) == '[') {
                kvKw = strTerm.split("],");
                kv = qt + kvKw[0] + "]" + qt;
                kw = qt + kvKw[1] + qt;
                qStr = qStr + kv + "," + kw + ":";
            } else {
                kvKw = strTerm.split(",");
                if (kvKw.length != 2) {
                    errmsg = "Invalid JSON string detected " + str;
                    error.setError(getClass().getName(), errmsg);
                    return str;
                }
                kv = qt + kvKw[0] + qt;
                kw = qt + kvKw[1] + qt;
                qStr = qStr + kv + "," + kw + ":";
            }
        }
        // last term has no key word
        int slen = 0;
        if (strParts[np].charAt(0) == '[') {
            kvKw = strParts[np].split("],");
            slen = kvKw[0].length();
            if (kvKw[0].charAt(slen - 1) == '}') {
                kv = qt + kvKw[0].substring(0, slen - 1) + qt;
                kv = kv + "}";
            } else {
                kv = qt + kvKw[0] + qt + "}";
            }
            qStr = qStr + kv;
        } else {
            kvKw = strParts[np].split(",");
            slen = kvKw[0].length();
            if (kvKw[0].charAt(slen - 1) == '}') {
                kv = qt + kvKw[0].substring(0, slen - 1) + qt;
                kv = kv + "}";
            } else
                kv = qt + kvKw[0] + qt + "}";
            qStr = qStr + kv;
        }
        if (qStr == null) {
            errmsg = "Invalid JSON string detected " + str;
            error.setError(getClass().getName(), errmsg);
        }
        return qStr;
    }

    String quotizeJsonString(String str) {
        String qStr = "";
        String qt = "\"";
        String kw = "", kv = "";
        String errmsg;
        if (str == "") {
            errmsg = "No Json string was specified: " + str;
            error.setError(getClass().getName(), errmsg);
            return str;
        }
        // str = {param:value, .... param:value}
        String[] paramVals = str.split(":"); // [0] = {param1, [1][0]= value0 [1][1] param2
        int noParams = paramVals.length;
        if (noParams < 2) {
            errmsg = "Invalid JSON string - missing semi-colon separator: " + str;
            error.setError(getClass().getName(), errmsg);
            return str;
        }
        if (paramVals[0].charAt(0) != '{') {
            errmsg = "Invalid JSON request - missing opening { on request: " + str;
            error.setError(getClass().getName(), errmsg);
            return str;
        }
        int pn = 0;
        String paramTerm;
        String paramVal = "";
        String paramKw = "", paramKvNext, paramKv = "";
        String paramKwNext = "";
        String[] pair = null;
        // get the next key word and key value

        StringTokenizer st;
        paramKw = paramVals[0].substring(1);
        pn = 1;
        if (paramVals[1].charAt(0) == '[') {
            pair = paramVals[1].split("\\],");
            paramKv = pair[0] + "]";
            qStr = "{" + qt + paramKw + qt + ":" + qt + paramKv + qt + "}";
            paramKwNext = pair[1];
        } else {
            pair = paramVals[1].split(",");
            paramKv = pair[0];
            paramKwNext = pair[1];
        }
        qStr = "{" + qt + paramKw + qt + ":" + qt + paramKv + qt + ",";
        paramKw = paramKwNext;
        while (++pn < noParams - 1) {
            // get next set of key value key word paris
            if (paramVals[pn].charAt(0) == '[') {
                st = new StringTokenizer(paramVals[pn], "]");
                paramKv = st.nextToken() + "]";
                paramKwNext = st.nextToken();
                paramKwNext = paramKwNext.replaceAll(",", "");
            } else {
                pair = paramVals[pn].split(",");
                paramKv = pair[0];
                paramKwNext = pair[1];
            }
            qStr += qt + paramKw + qt + ":" + qt + paramKv + qt + ",";
            paramKw = paramKwNext;
        }
        int len = paramVals[noParams - 1].length();
        paramKv = paramVals[noParams - 1].substring(0, len - 1);
        qStr += qt + paramKw + qt + ":" + qt + paramKv + qt + "}";
        return qStr;
    }

}
