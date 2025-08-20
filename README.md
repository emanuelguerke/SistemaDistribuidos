# SistemaDistribuidos
Matéria sistema distribuídos Projeto ordem de serviço UTFPR 2025-1 

Como usar 


abrir o xampp, ligar o MySQL e o Apache e criar Database "ordemdb"


Importar arquivo "ordemdb.sql" que está na raiz do projeto OrdemDeServiçoServidor

Extrair o arquivo para algum diretorio

Abrir o projeto no Eclipse, ir em propriedades do projeto e adicionar as bibliotecas jsons-simple e mysqlconector ao path da classe que se encontram na pasta lib dentro de src

Realizar o passo acima para OrdemDeServiço (que é o cliente) colocando o json-simple no path da classe


para executar o cliente OrdemDeServiço>src>gui>ConectarClienteWindow

rodar ConectarClienteWindow


para executar o servidor OrdemDeServiçoServidor>src>gui>ServidorGui

rodar ServidorGui


usuarios para teste
**Admin**
Usuario: admin
Senha: 123456

**Comum**
Usuario: comum
Senha: 123456


