-- 1. Adiciona a coluna de foto no usuário
ALTER TABLE usuarios 
ADD COLUMN foto_url VARCHAR(1000);

-- 2. Adiciona a coluna de biografia no profissional
ALTER TABLE profissionais 
ADD COLUMN biografia VARCHAR(255);

-- 3. Cria a nova tabela de avaliações
CREATE TABLE avaliacoes (
    id BIGSERIAL PRIMARY KEY,
    nota INT NOT NULL,
    comentario VARCHAR(500),
    data_avaliacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    profissional_id BIGINT NOT NULL REFERENCES profissionais(id),
    cliente_id BIGINT NOT NULL REFERENCES usuarios(id)
);