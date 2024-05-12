import cadastro.model.PessoaFisicaDAO;
import cadastro.model.PessoaJuridicaDAO;
import cadastro.model.util.ConectorBD;
import cadastro.model.util.SequenceManager;
import cadastrodb.model.PessoaFisica;
import cadastrodb.model.PessoaJuridica;

import java.util.List;
import java.util.Scanner;

public class CadastroDBTeste2 {
    public static void main(String[] args) {
        ConectorBD conectorBD = new ConectorBD();
        SequenceManager sequenceManager = new SequenceManager(conectorBD);
        PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO(conectorBD, sequenceManager);
        PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO(conectorBD, sequenceManager);

        Scanner scanner = new Scanner(System.in);

        while(true) {
            try {
                int opcaoMenu = mostrarMenu(scanner);

                scanner.nextLine();

                if (opcaoMenu == 0) {
                    break;
                }

                String tipoPessoa = tipoPessoa(scanner);

                switch (opcaoMenu) {
                    case 1:
                        inserir(pessoaFisicaDAO, pessoaJuridicaDAO, scanner, tipoPessoa);
                        break;
                    case 2:
                        alterar(pessoaFisicaDAO, pessoaJuridicaDAO, scanner, tipoPessoa);
                        break;
                    case 3:
                        excluirPorId(pessoaFisicaDAO, pessoaJuridicaDAO, scanner, tipoPessoa);
                        break;
                    case 4:
                        buscarPorId(pessoaFisicaDAO, pessoaJuridicaDAO, scanner, tipoPessoa);
                        break;
                    case 5:
                        exibirTodos(pessoaFisicaDAO, pessoaJuridicaDAO, tipoPessoa);
                        break;
                }
            } catch (Exception err) {
                System.out.println("Opção inválida, tente novamente.");
            }
        }
    }

    public static int mostrarMenu(Scanner scanner) {
        System.out.println("=================================");
        System.out.println("1 - Incluir Pessoa");
        System.out.println("2 - Alterar Pessoa");
        System.out.println("3 - Excluir Pessoa");
        System.out.println("4 - Buscar pelo Id");
        System.out.println("5 - Exibir Todos");
        System.out.println("0 - Finalizar Programa");
        System.out.println("=================================");

        return scanner.nextInt();
    }

    public static String tipoPessoa(Scanner scanner) throws Exception {
        System.out.println("F - Pessoa Física | J - Pessoa Jurídica");
        String tipoPessoa = scanner.nextLine();
        if(tipoPessoa.equals("F") || tipoPessoa.equals("J")) return tipoPessoa;
        throw new Exception("Tipo de Pessoa inválida");
    }

    public static void inserir(PessoaFisicaDAO pessoaFisicaDAO, PessoaJuridicaDAO pessoaJuridicaDAO, Scanner scanner, String tipoPessoa) {
        exibirTitulo("Insira os dados da Pessoa " + tipoPessoaString(tipoPessoa));

        System.out.println("Digite o nome:");
        String nome = scanner.nextLine();

        System.out.println("Digite o logradouro:");
        String logradouro = scanner.nextLine();

        System.out.println("Digite a cidade:");
        String cidade = scanner.nextLine();

        System.out.println("Digite o estado (2 caracteres):");
        String estado = scanner.nextLine();

        System.out.println("Digite o telefone:");
        String telefone = scanner.nextLine();

        System.out.println("Digite o email:");
        String email = scanner.nextLine();

        if(tipoPessoa.equals("F")) {
            PessoaFisica pessoaFisica = criarPF(scanner, 0, nome, logradouro, cidade, estado, telefone, email);
            pessoaFisicaDAO.inserir(pessoaFisica);
        } else {
            PessoaJuridica pessoaJuridica = criarPJ(scanner, 0, nome, logradouro, cidade, estado, telefone, email);
            pessoaJuridicaDAO.inserir(pessoaJuridica);
        }
        System.out.println();
    }

    private static void alterar(PessoaFisicaDAO pessoaFisicaDAO, PessoaJuridicaDAO pessoaJuridicaDAO, Scanner scanner, String tipoPessoa) {
        exibirTitulo("Insira os dados da " + tipoPessoaString(tipoPessoa));

        System.out.println("Digite o ID:");
        int id = scanner.nextInt();

        scanner.nextLine();

        System.out.println("Digite o nome:");
        String nome = scanner.nextLine();

        System.out.println("Digite o logradouro:");
        String logradouro = scanner.nextLine();

        System.out.println("Digite a cidade:");
        String cidade = scanner.nextLine();

        System.out.println("Digite o estado (2 caracteres):");
        String estado = scanner.nextLine();

        System.out.println("Digite o telefone:");
        String telefone = scanner.nextLine();

        System.out.println("Digite o email:");
        String email = scanner.nextLine();

        if(tipoPessoa.equals("F")) {
            PessoaFisica pessoaFisica = criarPF(scanner, id, nome, logradouro, cidade, estado, telefone, email);
            pessoaFisicaDAO.alterar(pessoaFisica);
        } else {
            PessoaJuridica pessoaJuridica = criarPJ(scanner, id, nome, logradouro, cidade, estado, telefone, email);
            pessoaJuridicaDAO.alterar(pessoaJuridica);
        }
        System.out.println();
    }

    private static void excluirPorId(PessoaFisicaDAO pessoaFisicaDAO, PessoaJuridicaDAO pessoaJuridicaDAO, Scanner scanner, String tipoPessoa) {
        System.out.println("Digite o ID:");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(tipoPessoa.equals("F")) {
            pessoaFisicaDAO.excluir(id);
        } else {
            pessoaJuridicaDAO.excluir(id);
        }
        System.out.println();
    }

    private static void buscarPorId(PessoaFisicaDAO pessoaFisicaDAO, PessoaJuridicaDAO pessoaJuridicaDAO, Scanner scanner, String tipoPessoa) {
        System.out.println("Digite o ID:");
        int id = scanner.nextInt();
        scanner.nextLine();

        exibirTitulo("Exibindo Pessoa " + tipoPessoaString(tipoPessoa));

        if(tipoPessoa.equals("F")) {
            PessoaFisica pessoaFisica = pessoaFisicaDAO.getPessoa(id);
            if(pessoaFisica == null) {
                System.out.println("Nenhuma Pessoa Física encontrada com o id " + id);
            } else {
                pessoaFisica.exibir();
            }
        }
        else {
            PessoaJuridica pessoaJuridica = pessoaJuridicaDAO.getPessoa(id);
            if(pessoaJuridica == null) {
                System.out.println("Nenhuma Pessoa Jurídica encontrada com o id " + id);
            } else {
                pessoaJuridica.exibir();
            }
        }
        System.out.println();
    }

    private static void exibirTodos(PessoaFisicaDAO pessoaFisicaDAO, PessoaJuridicaDAO pessoaJuridicaDAO, String tipoPessoa) {
        exibirTitulo("Exibindo dados de Pessoas " + tipoPessoaString(tipoPessoa) + "s");
        if(tipoPessoa.equals("F")) {
            List<PessoaFisica> pessoaFisicaList = pessoaFisicaDAO.getPessoas();
            pessoaFisicaList.forEach(pessoaFisica -> {
                pessoaFisica.exibir();
                System.out.println();
            });
        } else {
            List<PessoaJuridica> pessoaJuridicaList = pessoaJuridicaDAO.getPessoas();
            pessoaJuridicaList.forEach(pessoaJuridica -> {
                pessoaJuridica.exibir();
                System.out.println();
            });
        }
    }

    private static PessoaFisica criarPF(Scanner scanner, int id, String nome, String logradouro, String cidade, String estado, String telefone, String email) {
        System.out.println("Digite o CPF:");
        String cpf = scanner.nextLine();

        return new PessoaFisica(id, nome, logradouro, cidade, estado, telefone, email, cpf);
    }

    private static PessoaJuridica criarPJ(Scanner scanner, int id, String nome, String logradouro, String cidade, String estado, String telefone, String email) {
        System.out.println("Digite o CNPJ:");
        String cnpj = scanner.nextLine();

        return new PessoaJuridica(id, nome, logradouro, cidade, estado, telefone,email, cnpj);
    }

    private static void exibirTitulo(String msg) {
        System.out.println(msg);
        System.out.println("=================================");
    }

    private static String tipoPessoaString(String tipoPessoa) {
        return tipoPessoa.equals("F") ? "Física" : "Jurídica";
    }
}
