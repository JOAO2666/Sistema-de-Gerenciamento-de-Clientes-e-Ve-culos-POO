# Requisitos do Sistema

Projeto: Sistema de Gerenciamento de Clientes, Veículos e Seguros
Disciplina: Programação Orientada a Objetos (POO)
Docente: Prof.ª Ana Emília de Melo Queiroz

## 1. Descrição geral

O sistema foi desenvolvido em Java para gerenciar o cadastro de clientes de uma corretora/seguradora, seus bens (veículos e imóveis), a emissão de apólices de seguro, o controle de parcelas e a abertura de sinistros. A aplicação roda via console e salva os dados em arquivos de texto na pasta `data/`.

## 2. Requisitos funcionais

### Clientes e bens seguráveis
* RF01 - Cadastrar cliente: permitir o cadastro de pessoa física (CPF, RG, data de nascimento, sexo e estado civil) e pessoa jurídica (CNPJ, razão social, inscrição estadual, nome fantasia e representante).
* RF02 - Consultar e listar clientes: listar todos os clientes cadastrados e buscar por ID ou por CPF/CNPJ.
* RF03 - Atualizar e remover cliente: alterar telefone, e-mail e endereço de um cliente existente ou removê-lo do cadastro, apagando também os veículos vinculados.
* RF04 - Cadastrar veículo: vincular um veículo (marca, modelo, ano, placa, renavam, chassi e valor FIPE) a um cliente já cadastrado.
* RF05 - Cadastrar imóvel: informar os dados do imóvel (CEP, endereço, cidade, estado, área, valor de mercado e tipo de construção) na contratação do seguro residencial.

### Apólices e financeiro
* RF06 - Emitir apólice de automóvel: gerar apólice vinculada a um cliente e a um veículo, considerando classe de bônus e perfil do condutor no cálculo do prêmio.
* RF07 - Emitir apólice residencial: gerar apólice vinculada a um imóvel, considerando o tipo de residência e presença de alarme.
* RF08 - Emitir apólice de vida: gerar apólice de vida para o cliente, calculando o prêmio conforme capital segurado, idade e indicador de fumante.
* RF09 - Gerar e quitar parcelas: dividir o valor total do prêmio na quantidade de parcelas escolhida e permitir registrar o pagamento de cada parcela.
* RF10 - Cancelar apólice: permitir o cancelamento de uma apólice ativa pelo seu ID.

### Sinistros e relatórios
* RF11 - Registrar sinistro: abrir ocorrência para uma apólice ativa informando data, tipo de sinistro, descrição e valor estimado do prejuízo.
* RF12 - Designar perito e liquidar sinistro: associar um perito ao sinistro para registrar o laudo de aprovação ou reprovação e definir o valor indenizado.
* RF13 - Exibir relatório geral: mostrar o resumo de clientes, veículos, apólices por ramo, valores de prêmios, parcelas recebidas e sinistros registrados.

## 3. Requisitos não funcionais

* RNF01 - Linguagem: Java (compatível com JDK 11 ou superior).
* RNF02 - Organização do código: separação em pacotes (`model`, `repository`, `service`, `controller`, `view` e `exception`).
* RNF03 - Persistência: gravação e leitura em arquivos `.txt` separados por ponto e vírgula (`;`) no diretório `data/`.
* RNF04 - Tratamento de erros no console: validação de entradas numéricas e de datas no menu para evitar que o programa encerre por erro de digitação.

## 4. Regras de negócio

* RN01 - Identificação sequencial: o ID de cada entidade é gerado automaticamente a partir do arquivo `sequencias.txt`.
* RN02 - Vínculo de veículo: só é possível cadastrar um veículo para um cliente que já exista no sistema.
* RN03 - Cálculo de prêmio: cada tipo de apólice calcula seu prêmio base de acordo com suas próprias regras, somando as coberturas adicionais e o IOF de 7,38%.
* RN04 - Vigência no sinistro: não é permitido registrar sinistro para apólice cancelada, suspensa ou fora do período de vigência (`ApoliceVencidaException`).
* RN05 - Sinistro duplicado: o sistema bloqueia o registro de dois sinistros do mesmo tipo e na mesma data para a mesma apólice (`SinistroJaRegistradoException`).
