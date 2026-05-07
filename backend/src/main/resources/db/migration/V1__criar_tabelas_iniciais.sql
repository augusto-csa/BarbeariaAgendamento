CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL -- Ex: CLIENTE, BARBEIRO, ADMIN
);

CREATE TABLE profissionais (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT UNIQUE NOT NULL REFERENCES usuarios(id),
    ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE servicos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    preco DECIMAL(10, 2) NOT NULL,
    duracao_minutos INT NOT NULL
);

CREATE TABLE agendamentos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL REFERENCES usuarios(id),
    profissional_id BIGINT NOT NULL REFERENCES profissionais(id),
    data_hora TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL -- Ex: PENDENTE, CONFIRMADO, CANCELADO
);

CREATE TABLE agendamentos_servicos (
    agendamento_id BIGINT NOT NULL REFERENCES agendamentos(id),
    servico_id BIGINT NOT NULL REFERENCES servicos(id),
    PRIMARY KEY (agendamento_id, servico_id)
);