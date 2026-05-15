ALTER TABLE monitoramentos ADD CONSTRAINT fk_monitoramentos_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id);
ALTER TABLE monitoramentos ADD CONSTRAINT fk_monitoramentos_advogado FOREIGN KEY (advogado_id) REFERENCES usuarios(id);
