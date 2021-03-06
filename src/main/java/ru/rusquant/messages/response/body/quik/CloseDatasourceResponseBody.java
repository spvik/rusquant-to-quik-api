package ru.rusquant.messages.response.body.quik;

import ru.rusquant.data.quik.BooleanResult;
import ru.rusquant.messages.response.body.ResponseBody;

/**
 * Author: Aleksey Kutergin <aleksey.v.kutergin@gmail.ru>
 * Company: Rusquant
 */
public class CloseDatasourceResponseBody extends ResponseBody {

    private BooleanResult result;

    public CloseDatasourceResponseBody() {

    }

    public CloseDatasourceResponseBody(BooleanResult result) {
        this.result = result;
    }

    public BooleanResult getResult() {
        return result;
    }

    public void setResult(BooleanResult result) {
        this.result = result;
    }
}
