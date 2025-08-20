-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 19/06/2025 às 01:50
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `ordemdb`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `ordem`
--

CREATE TABLE `ordem` (
  `id` int(11) NOT NULL,
  `autor` varchar(100) NOT NULL,
  `descricao` varchar(200) NOT NULL,
  `status` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Despejando dados para a tabela `ordem`
--

INSERT INTO `ordem` (`id`, `autor`, `descricao`, `status`) VALUES
(1, 'admin', 'arrumar a porta da garagem', 'pendente');

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuario`
--

CREATE TABLE `usuario` (
  `nome_usuario` varchar(100) NOT NULL,
  `senha` varchar(100) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `perfil` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Despejando dados para a tabela `usuario`
--

INSERT INTO `usuario` (`nome_usuario`, `senha`, `nome`, `perfil`) VALUES
('admin', '123456', 'admin', 'adm'),
('and', 'andre', 'and', 'comum'),
('comum', '123456', 'comum', 'comum'),
('craudio', '123456', 'junior', 'adm'),
('emanuel', 'emanuel', 'emanuel', 'adm'),
('emanuelusuario', 'emanuel', 'emanuelnome', 'adm'),
('teste1', 'teste1','teste1', 'adm');

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `ordem`
--
ALTER TABLE `ordem`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_autor` (`autor`);

--
-- Índices de tabela `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`nome_usuario`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `ordem`
--
ALTER TABLE `ordem`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `ordem`
--
ALTER TABLE `ordem`
  ADD CONSTRAINT `fk_autor` FOREIGN KEY (`autor`) REFERENCES `usuario` (`nome_usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
