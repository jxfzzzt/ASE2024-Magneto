package zing.client;

public class Output {

    String message;

    byte code;

    Exception exception;

    private static OutputHandler handler;

    private Output(String str) {
        message = str;
    }

    /**
     * 获取状态码
     * @return 0正常 1异常
     */
    public byte getCode() {
        return code;
    }

    public static void print(Output output) {
        handler.handle(output);
    }

    public static void print(String str) {
        print(Output.info(str));
    }

    public static void setHandler(OutputHandler handler) {
        Output.handler = handler;
    }

    public static Output info(String str) {
        Output output = new Output(str);
        output.code = 0;
        return output;
    }

    public static Output warn(String str, Exception e) {
        Output output = new Output(str);
        output.code = 1;
        output.exception = e;
        return output;
    }

}
