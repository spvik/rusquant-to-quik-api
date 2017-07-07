package ru.rusquant.messages.request.body;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import ru.rusquant.data.quik.Transaction;

/**
 *   Single transaction request
 *   Author: Aleksey Kutergin <aleksey.v.kutergin@gmail.ru>
 *   Company: Rusquant
 */
@JsonAutoDetect
public class TransactionRequestBody extends RequestBody
{
    Transaction transaction;

    public TransactionRequestBody()
    {

    }

    public TransactionRequestBody(Transaction transaction)
    {
        this.transaction = transaction;
    }

    public Transaction getTransaction()
    {
        return transaction;
    }

    public void setTransaction(Transaction transaction)
    {
        this.transaction = transaction;
    }
}
