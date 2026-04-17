-- 1. Inserir Usuários (Senhas mockadas como '123456' para teste)
INSERT INTO usuarios (nome, email, telefone, senha, role) VALUES
('João Chefe (Admin)', 'admin@barbearia.com', '11999999999', '123456', 'ADMIN'),
('Carlos Barbeiro', 'carlos@barbearia.com', '11988888888', '123456', 'BARBEIRO'),
('Pedro Cliente', 'pedro@gmail.com', '11977777777', '123456', 'CLIENTE');

-- 2. Transformar o usuário 2 (Carlos) em um Profissional
INSERT INTO profissionais (usuario_id, ativo) VALUES
(2, true);

-- 3. Inserir Serviços base da barbearia
INSERT INTO servicos (nome, descricao, preco, duracao_minutos) VALUES
('Corte Degradê', 'Corte moderno na máquina e tesoura', 45.00, 40),
('Barba Terapia', 'Alinhamento com toalha quente e navalha', 35.00, 30),
('Sobrancelha', 'Limpeza rápida na navalha', 15.00, 10);

-- 4. Inserir um Agendamento (O Pedro marcou com o Carlos para amanhã)
-- O PostgreSQL entende a soma de datas usando o comando INTERVAL
INSERT INTO agendamentos (cliente_id, profissional_id, data_hora, status) VALUES
(3, 1, NOW() + INTERVAL '1 day', 'CONFIRMADO');

-- 5. Ligar os Serviços ao Agendamento (Pedro vai fazer Corte e Barba no mesmo horário)
INSERT INTO agendamentos_servicos (agendamento_id, servico_id) VALUES
(1, 1), -- Corte Degradê
(1, 2); -- Barba Terapia