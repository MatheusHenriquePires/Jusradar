CREATE TABLE monitoramentos (
    id UUID PRIMARY KEY,
    documento VARCHAR(20) NOT NULL,
    tribunal VARCHAR(20) NOT NULL,
    criado_em TIMESTAMP NOT NULL
);