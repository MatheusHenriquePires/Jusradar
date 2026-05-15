ALTER TABLE monitoramentos ADD COLUMN ultima_movimentacao VARCHAR(255);
ALTER TABLE monitoramentos ADD COLUMN ultima_consulta TIMESTAMP;
ALTER TABLE monitoramentos ADD COLUMN cliente_id BIGINT;
ALTER TABLE monitoramentos ADD COLUMN advogado_id UUID;
