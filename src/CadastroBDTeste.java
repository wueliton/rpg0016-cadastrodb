import cadastro.model.PessoaFisicaDAO;
import cadastro.model.PessoaJuridicaDAO;
import cadastro.model.util.ConectorBD;
import cadastro.model.util.SequenceManager;
import cadastrodb.model.PessoaFisica;
import cadastrodb.model.PessoaJuridica;

import java.sql.SQLException;
import java.util.List;

public class CadastroBDTeste {
    public static void main(String[] args) {
        ConectorBD conectorBD = new ConectorBD();
        SequenceManager sequenceManager = new SequenceManager(conectorBD);
        PessoaFisicaDAO pessoaFisicaDAO = new PessoaFisicaDAO(conectorBD, sequenceManager);
        PessoaJuridicaDAO pessoaJuridicaDAO = new PessoaJuridicaDAO(conectorBD, sequenceManager);

        // Instanciar uma pessoa física e persistir no banco de dados.
        PessoaFisica pessoaFisica = new PessoaFisica(0, "Ribeiro", "Rua 30, Centro", "São Paulo", "SP", "1111-1111", "ribeiro@sp.com.br", "11111111111");
        pessoaFisica = pessoaFisicaDAO.inserir(pessoaFisica);

        // Alterar os dados da pessoa física no banco.
        pessoaFisica.setNome("Ribeiro Alberto");
        pessoaFisica.setCidade("Guarulhos");
        pessoaFisicaDAO.alterar(pessoaFisica);

        // Consultar todas as pessoas físicas do banco de dados e listar no console.
        List<PessoaFisica> listaPF = pessoaFisicaDAO.getPessoas();
        listaPF.forEach(PessoaFisica::exibir);

        // Excluir a pessoa física criada anteriormente no banco.
        pessoaFisicaDAO.excluir(pessoaFisica.getId());

        // Instanciar uma pessoa jurídica e persistir no banco de dados.
        PessoaJuridica pessoaJuridica = new PessoaJuridica(0, "Ribeiro LTDA", "Rua 30, Centro", "São Paulo", "SP", "1111-1111", "ribeiro-ltda@sp.com.br", "11111111111111");
        pessoaJuridica = pessoaJuridicaDAO.inserir(pessoaJuridica);

        // Alterar os dados da pessoa jurídica no banco.
        pessoaJuridica.setNome("Ribeiro Soares LTDA");
        pessoaJuridica.setLogradouro("Rua 31, Centro");
        pessoaFisicaDAO.alterar(pessoaFisica);

        // Consultar todas as pessoas jurídicas do banco e listar no console.
        List<PessoaJuridica> listaPJ = pessoaJuridicaDAO.getPessoas();
        listaPJ.forEach(PessoaJuridica::exibir);

        // Excluir a pessoa jurídica criada anteriormente no banco.
        pessoaJuridicaDAO.excluir(pessoaJuridica.getId());

        conectorBD.close(conectorBD.getConnection());
    }
}
