package cadastro.model;

import cadastro.model.util.ConectorBD;
import cadastro.model.util.SequenceManager;
import cadastrodb.model.PessoaJuridica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PessoaJuridicaDAO {
    private ConectorBD conectorBD;
    private SequenceManager sequenceManager;

    public PessoaJuridicaDAO(ConectorBD conectorBD, SequenceManager sequenceManager) {
        this.conectorBD = conectorBD;
        this.sequenceManager = sequenceManager;
    }

    public PessoaJuridica getPessoa(int id) {
        PessoaJuridica pessoaJuridica = null;
        String sql = "SELECT * FROM pessoa AS p INNER JOIN pessoaJuridica AS pj ON p.idPessoa = pj.idPessoa WHERE p.idPessoa = ?";
        try {
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.first()) {
                pessoaJuridica = converterPessoa(resultSet);
            }
            conectorBD.close(preparedStatement);
            conectorBD.close(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoaJuridica;
    }

    public List<PessoaJuridica> getPessoas() {
        List<PessoaJuridica> pessoas = new ArrayList<PessoaJuridica>();
        String sql = "SELECT * FROM pessoa AS p INNER JOIN pessoaJuridica AS pj ON p.idPessoa = pj.idPessoa";
        try(ResultSet resultSet = conectorBD.getSelect(sql)) {
            while(resultSet.next()) {
                pessoas.add(converterPessoa(resultSet));
            }
            conectorBD.close(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoas;
    }

    public PessoaJuridica inserir(PessoaJuridica pessoa) {
        try {
            int idPessoa = sequenceManager.getValue("idPessoa");
            pessoa.setId(idPessoa);
            String sql = "INSERT INTO pessoa (idPessoa, nome, logradouro, cidade, estado, telefone, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setInt(1, idPessoa);
            preparedStatement.setString(2, pessoa.getNome());
            preparedStatement.setString(3, pessoa.getLogradouro());
            preparedStatement.setString(4, pessoa.getCidade());
            preparedStatement.setString(5, pessoa.getEstado());
            preparedStatement.setString(6, pessoa.getTelefone());
            preparedStatement.setString(7, pessoa.getEmail());
            preparedStatement.executeUpdate();
            conectorBD.close(preparedStatement);

            String sqlPJ = "INSERT INTO pessoaJuridica (idPessoa, cnpj) VALUES (?, ?)";
            PreparedStatement preparedStatementPJ = conectorBD.getPrepared(sqlPJ);
            preparedStatementPJ.setInt(1, idPessoa);
            preparedStatementPJ.setString(2, pessoa.getCnpj());
            preparedStatementPJ.executeUpdate();
            conectorBD.close(preparedStatementPJ);

            System.out.println("Pessoa Jurídica salva com sucesso");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pessoa;
    }

    public void alterar(PessoaJuridica pessoa) {
        try {
            String sql = "UPDATE pessoa SET nome = ?, logradouro = ?, cidade = ?, estado = ?, telefone = ?, email = ? WHERE idPessoa = ?";
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setString(1, pessoa.getNome());
            preparedStatement.setString(2, pessoa.getLogradouro());
            preparedStatement.setString(3, pessoa.getCidade());
            preparedStatement.setString(4, pessoa.getEstado());
            preparedStatement.setString(5, pessoa.getTelefone());
            preparedStatement.setString(6, pessoa.getEmail());
            preparedStatement.setInt(7, pessoa.getId());
            preparedStatement.executeUpdate();
            conectorBD.close(preparedStatement);

            String sqlPJ = "UPDATE pessoaJuridica SET cnpj = ? WHERE idPessoa = ?";
            PreparedStatement preparedStatementPJ = conectorBD.getPrepared(sqlPJ);
            preparedStatementPJ.setString(1, pessoa.getCnpj());
            preparedStatementPJ.setInt(2, pessoa.getId());
            preparedStatementPJ.executeUpdate();
            conectorBD.close(preparedStatementPJ);

            System.out.println("Pessoa Jurídica atualizada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        try {
            String sqlPf = "DELETE FROM pessoaJuridica WHERE idPessoa = ?";
            PreparedStatement preparedStatementPJ = conectorBD.getPrepared(sqlPf);
            preparedStatementPJ.setInt(1, id);
            preparedStatementPJ.executeUpdate();

            String sql = "DELETE FROM pessoa WHERE idPessoa = ?";
            PreparedStatement preparedStatement = conectorBD.getPrepared(sql);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            conectorBD.close(preparedStatement);

            System.out.println("Pessoa Jurídica excluída com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PessoaJuridica converterPessoa(ResultSet resultSet) throws SQLException {
        PessoaJuridica pessoa = new PessoaJuridica();
        pessoa.setId(resultSet.getInt("idPessoa"));
        pessoa.setNome(resultSet.getString("nome"));
        pessoa.setLogradouro(resultSet.getString("logradouro"));
        pessoa.setCidade(resultSet.getString("cidade"));
        pessoa.setEstado(resultSet.getString("estado"));
        pessoa.setTelefone(resultSet.getString("telefone"));
        pessoa.setEmail(resultSet.getString("email"));
        pessoa.setCnpj(resultSet.getString("cnpj"));
        return pessoa;
    }
}
