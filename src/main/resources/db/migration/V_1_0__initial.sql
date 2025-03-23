SET search_path TO public;

CREATE TABLE public.ACCOUNT
(
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    version integer NOT NULL DEFAULT 0,
    balance numeric(15,2) NOT NULL
);

CREATE TABLE transaction (
    transaction_id UUID PRIMARY KEY,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_sender FOREIGN KEY (sender_id) REFERENCES account(id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_receiver FOREIGN KEY (receiver_id) REFERENCES account(id) ON DELETE CASCADE
);
