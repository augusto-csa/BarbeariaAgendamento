------------------------------------ BARBEIROS ----------------------------------

INSERT INTO servicos (id, nome, descricao, preco, duracao_minutos) VALUES
(10, 'Corte Máquina Rápido', 'Apenas um pente, sem frescura', 30.00, 15),
(11, 'Corte Clássico', 'Social na tesoura e máquina', 45.00, 30),
(12, 'Barba Simples', 'Aparo rápido na máquina', 20.00, 15),
(13, 'Barba Terapia', 'Com toalha quente, navalha e massagem', 40.00, 30),
(14, 'Combo Premium (Corte + Barba)', 'Serviço completo e detalhista', 85.00, 60),
(15, 'Platinado / Química', 'Descoloração global e matização', 120.00, 60);

-- SENHAS: 123456
INSERT INTO usuarios (id, nome, email, telefone, senha, role) VALUES
(10, 'Marcos Navalha', 'marcos@barbearia.com', '11911111111', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'BARBEIRO'),
(11, 'Tiago Fade', 'tiago@barbearia.com', '11922222222', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'BARBEIRO'),
(12, 'Beto Vintage', 'beto@barbearia.com', '11933333333', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'BARBEIRO');

INSERT INTO profissionais (id, usuario_id, ativo) VALUES
(10, 10, true),
(11, 11, true),
(12, 12, true);

INSERT INTO profissionais_servicos (profissional_id, servico_id) VALUES

-- Marcos
(10, 10), 
(10, 11), 
(10, 12), 
(10, 13),

-- Tiago
(11, 14), 
(11, 15),

-- Beto
(12, 11), 
(12, 13), 
(12, 14);

-- Marcos
INSERT INTO horarios_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim) VALUES 
(10, 'TUESDAY', '09:00:00', '12:00:00'), (10, 'TUESDAY', '13:00:00', '18:00:00'),
(10, 'WEDNESDAY', '09:00:00', '12:00:00'), (10, 'WEDNESDAY', '13:00:00', '18:00:00'),
(10, 'THURSDAY', '09:00:00', '12:00:00'), (10, 'THURSDAY', '13:00:00', '18:00:00'),
(10, 'FRIDAY', '09:00:00', '12:00:00'), (10, 'FRIDAY', '13:00:00', '18:00:00'),
(10, 'SATURDAY', '09:00:00', '12:00:00'), (10, 'SATURDAY', '13:00:00', '18:00:00');

-- Tiago
INSERT INTO horarios_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim) VALUES 
(11, 'MONDAY', '10:00:00', '16:00:00'),
(11, 'TUESDAY', '10:00:00', '16:00:00'),
(11, 'WEDNESDAY', '10:00:00', '16:00:00'),
(11, 'THURSDAY', '10:00:00', '16:00:00'),
(11, 'FRIDAY', '10:00:00', '16:00:00');

-- Beto
INSERT INTO horarios_trabalho (profissional_id, dia_semana, hora_inicio, hora_fim) VALUES 
(12, 'SATURDAY', '08:00:00', '14:00:00'),
(12, 'SUNDAY', '08:00:00', '14:00:00');]

------------------------------------ CLIENTES ----------------------------------

-- SENHAS: 123456
INSERT INTO usuarios (id, nome, email, telefone, senha, role) VALUES
(20, 'Lucas Ferreira', 'lucas@gmail.com', '11944444444', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'CLIENTE'),
(21, 'Rafael Sousa', 'rafael@gmail.com', '11955555555', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'CLIENTE'),
(22, 'Gabriel Almeida', 'gabriel@gmail.com', '11966666666', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'CLIENTE'),
(23, 'Bruno Costa', 'bruno@gmail.com', '11977777777', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'CLIENTE'),
(24, 'Felipe Santos', 'felipe@gmail.com', '11988888888', '$2a$10$wL2PJ.VXzgS5JZhGqnCVMuwq92xbjfPiDBVHusxpoOoyLwqNpgTAO', 'CLIENTE');

INSERT INTO agendamentos (id, cliente_id, profissional_id, data_hora, status) VALUES
(10, 20, 10, CURRENT_DATE + INTERVAL '1 day' + INTERVAL '10 hours', 'CONFIRMADO'), -- Lucas com Marcos (Amanhã às 10h)
(11, 21, 10, CURRENT_DATE + INTERVAL '1 day' + INTERVAL '14 hours', 'CONFIRMADO'), -- Rafael com Marcos (Amanhã às 14h)
(12, 22, 11, CURRENT_DATE + INTERVAL '2 days' + INTERVAL '11 hours', 'CONFIRMADO'), -- Gabriel com Tiago (Daqui a 2 dias às 11h)
(13, 23, 12, CURRENT_DATE + INTERVAL '3 days' + INTERVAL '09 hours', 'CANCELADO');  -- Bruno com Beto (Cancelado)


INSERT INTO agendamentos_servicos (agendamento_id, servico_id) VALUES
-- Agendamento 10 (Marcos)
(10, 11), 

-- Agendamento 11 (Marcos)
(11, 12),
(11, 10),

-- Agendamento 12 (Tiago)
(12, 14),

-- Agendamento 13 (Beto - Cancelado)
(13, 11);


-- Continuar a contar a partir do ID mais alto
SELECT setval('usuarios_id_seq', (SELECT MAX(id) FROM usuarios));
SELECT setval('agendamentos_id_seq', (SELECT MAX(id) FROM agendamentos));