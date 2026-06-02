import { useEffect, useMemo, useRef, useState } from 'react';
import { api, getStoredUser, removeToken, saveToken } from './api.js';

const USER_NAME_KEY = 'jusradar_user_name';

const emptyMonitoramento = {
  numeroProcesso: '',
  tribunal: 'TJPI',
<<<<<<< HEAD
=======
  nomeCliente: '',
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
  documentoCliente: '',
};

const emptyDocumento = {
  numeroProcesso: '',
  tribunal: 'TJPI',
  nomeAdvogado: '',
  oabNumero: '',
  nomeCliente: '',
  pergunta: '',
  tipoDocumento: 'PETICAO_SIMPLES',
};

<<<<<<< HEAD
const menuItems = [
  { id: 'dashboard', label: 'Dashboard' },
  { id: 'processos', label: 'Processos' },
  { id: 'dashboard', label: 'Clientes' },
  { id: 'chat', label: 'Chat IA' },
  { id: 'documentos', label: 'Documentos' },
  { id: 'dashboard', label: 'Relatorios' },
  { id: 'dashboard', label: 'Configuracoes' },
];

function getClientName(item) {
  return item.cliente?.nome || item.documentoCliente || 'Cliente nao informado';
=======
const sidebarSections = [
  {
    label: 'Principal',
    items: [
      { id: 'dashboard', label: 'Dashboard', icon: 'DB' },
      { id: 'processos', label: 'Processos', icon: 'PR', badgeKey: 'processos' },
      { id: 'clientes', label: 'Clientes', icon: 'CL', route: 'dashboard' },
      { id: 'audiencias', label: 'Audiências', icon: 'AU', badgeKey: 'audiencias', route: 'dashboard' },
    ],
  },
  {
    label: 'Ferramentas',
    items: [
      { id: 'chat', label: 'Chat IA', icon: 'IA' },
      { id: 'documentos', label: 'Documentos', icon: 'DO' },
      { id: 'relatorios', label: 'Relatórios', icon: 'RG', route: 'dashboard' },
      { id: 'configuracoes', label: 'Configurações', icon: 'CF', route: 'dashboard' },
    ],
  },
];

function getClientName(item) {
  return item.cliente?.nome || 'Cliente nao informado';
}

function getClientDocument(item) {
  return item.cliente?.documento || item.documentoCliente || '-';
}

function getSidebarUserName(user) {
  return localStorage.getItem(USER_NAME_KEY) || user?.email || 'Matheus Henrique';
}

function getUserInitials(name) {
  return name
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((part) => part[0]?.toUpperCase() || '')
    .join('');
}

function Sidebar({ active, counts = {}, onNavigate, onLogout, user }) {
  const displayName = getSidebarUserName(user);
  const initials = getUserInitials(displayName) || 'MH';

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <div className="sidebar-logo" aria-hidden="true">
          <span />
        </div>
        <div className="sidebar-brand-text">
          <strong>JusRadar</strong>
        </div>
      </div>

      <div className="sidebar-sections">
        {sidebarSections.map((section) => (
          <div className="sidebar-group" key={section.label}>
            <span className="sidebar-label">{section.label}</span>
            <nav className="sidebar-nav" aria-label={section.label}>
              {section.items.map((item) => {
                const isActive = active === item.id;
                const count = item.badgeKey ? counts[item.badgeKey] : null;

                return (
                  <button
                    type="button"
                    key={item.id}
                    className={`sidebar-item${isActive ? ' active' : ''}`}
                    onClick={() => onNavigate(item.route || item.id)}
                  >
                    <span className="sidebar-item-icon" aria-hidden="true">
                      {item.icon}
                    </span>
                    <span className="sidebar-item-label">{item.label}</span>
                    {count ? <span className="sidebar-badge">{count}</span> : null}
                  </button>
                );
              })}
            </nav>
          </div>
        ))}
      </div>

      <div className="sidebar-footer">
        <div className="sidebar-user">
          <div className="sidebar-avatar">{initials}</div>
          <div className="sidebar-user-text">
            <strong>{displayName}</strong>
            <span>Advogado Senior</span>
          </div>
        </div>
        <button type="button" className="sidebar-more" onClick={onLogout} aria-label="Mais opcoes">
          ...
        </button>
      </div>
    </aside>
  );
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
}

function formatDate(value) {
  if (!value) {
    return '-';
  }

  return new Intl.DateTimeFormat('pt-BR').format(new Date(value));
}

function getStatus(item) {
  if (item.ultimaMovimentacao) {
    return {
      label: 'Em andamento',
      className: 'andamento',
    };
  }

  return {
    label: 'Monitorando',
    className: 'andamento',
  };
}

<<<<<<< HEAD
=======
function getInitialRoute() {
  if (window.location.pathname === '/reset-password') {
    return 'reset-password';
  }

  return 'login';
}

function getResetTokenFromUrl() {
  return new URLSearchParams(window.location.search).get('token') || '';
}

>>>>>>> 4bd12d3 (Atualização p deploy vercel)
function LoginPage({ onAuthenticated }) {
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({ nome: '', email: '', senha: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
<<<<<<< HEAD

  const isRegister = mode === 'register';
=======
  const [success, setSuccess] = useState('');

  const isRegister = mode === 'register';
  const isForgotPassword = mode === 'forgot-password';
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError('');
<<<<<<< HEAD
=======
    setSuccess('');
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

    try {
      if (isRegister) {
        await api.register({
          nome: form.nome,
          email: form.email,
          senha: form.senha,
        });

        localStorage.setItem(USER_NAME_KEY, form.nome);
      }

<<<<<<< HEAD
=======
      if (isForgotPassword) {
        await api.forgotPassword({
          email: form.email,
        });

        setSuccess(
          'Se o e-mail existir, enviaremos as instrucoes para redefinir sua senha.',
        );
        setForm({ nome: '', email: '', senha: '' });
        return;
      }

>>>>>>> 4bd12d3 (Atualização p deploy vercel)
      const response = await api.login({
        email: form.email,
        senha: form.senha,
      });

      saveToken(response.token);
      onAuthenticated(getStoredUser());
    } catch (err) {
      setError(
        err.message ||
          'Nao foi possivel entrar. Confira se o backend esta rodando.',
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-container">
        <div className="login-left">
          <h1>JusRadar</h1>
          <p>API REST para monitoramento judicial automatizado com IA.</p>

          <div className="info-box">Monitoramento de processos</div>
          <div className="info-box">Dashboard juridico inteligente</div>
          <div className="info-box">Assistente juridico com IA</div>
        </div>

        <form className="login-right" onSubmit={handleSubmit}>
<<<<<<< HEAD
          <h2>{isRegister ? 'Cadastro do Advogado' : 'Login do Advogado'}</h2>
          <p>Entre na plataforma para acompanhar seus processos.</p>
=======
          <h2>
            {isForgotPassword
              ? 'Recuperar senha'
              : isRegister
                ? 'Cadastro do Advogado'
                : 'Login do Advogado'}
          </h2>
          <p>
            {isForgotPassword
              ? 'Digite seu e-mail para receber as instrucoes de recuperacao.'
              : 'Entre na plataforma para acompanhar seus processos.'}
          </p>
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

          {isRegister && (
            <input
              type="text"
              value={form.nome}
              onChange={(event) => updateField('nome', event.target.value)}
              placeholder="Digite seu nome"
              aria-label="Nome"
              required
            />
          )}

          <input
            type="email"
            value={form.email}
            onChange={(event) => updateField('email', event.target.value)}
            placeholder="Digite seu e-mail"
            aria-label="E-mail"
            required
          />

<<<<<<< HEAD
          <input
            type="password"
            value={form.senha}
            onChange={(event) => updateField('senha', event.target.value)}
            placeholder="Digite sua senha"
            aria-label="Senha"
            required
          />

          {error && <div className="feedback error">{error}</div>}

          <button type="submit" disabled={loading}>
            {loading ? 'Conectando...' : isRegister ? 'Cadastrar e entrar' : 'Entrar'}
=======
          {!isForgotPassword && (
            <input
              type="password"
              value={form.senha}
              onChange={(event) => updateField('senha', event.target.value)}
              placeholder="Digite sua senha"
              aria-label="Senha"
              required
            />
          )}

          {error && <div className="feedback error">{error}</div>}
          {success && <div className="feedback success">{success}</div>}

          <button type="submit" disabled={loading}>
            {loading
              ? 'Processando...'
              : isForgotPassword
                ? 'Enviar link de recuperacao'
                : isRegister
                  ? 'Cadastrar e entrar'
                  : 'Entrar'}
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
          </button>

          <div className="register">
            {isRegister ? 'Ja possui conta?' : 'Nao possui conta?'}{' '}
            <button
              type="button"
              className="link-button"
              onClick={() => {
                setMode(isRegister ? 'login' : 'register');
                setError('');
<<<<<<< HEAD
=======
                setSuccess('');
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
              }}
            >
              {isRegister ? 'Entrar' : 'Cadastrar'}
            </button>
<<<<<<< HEAD
=======
            <button
              type="button"
              className="link-button forgot-link"
              onClick={() => {
                setMode('forgot-password');
                setError('');
                setSuccess('');
              }}
            >
              Esqueci minha senha
            </button>
            {isForgotPassword && (
              <button
                type="button"
                className="link-button back-link"
                onClick={() => {
                  setMode('login');
                  setError('');
                  setSuccess('');
                }}
              >
                Voltar para o login
              </button>
            )}
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
          </div>
        </form>
      </section>
    </main>
  );
}

<<<<<<< HEAD
=======
function ResetPasswordPage() {
  const [form, setForm] = useState({
    token: getResetTokenFromUrl(),
    senha: '',
    confirmarSenha: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    if (form.senha !== form.confirmarSenha) {
      setError('As senhas nao conferem.');
      setLoading(false);
      return;
    }

    try {
      await api.resetPassword({
        token: form.token,
        senha: form.senha,
      });

      setSuccess('Senha redefinida com sucesso. Agora voce pode entrar novamente.');
      setForm({ token: '', senha: '', confirmarSenha: '' });
    } catch (err) {
      setError(err.message || 'Nao foi possivel redefinir a senha.');
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="login-page">
      <section className="login-container">
        <div className="login-left">
          <h1>JusRadar</h1>
          <p>Redefinicao de senha com link seguro por e-mail.</p>

          <div className="info-box">Token com expiracao curta</div>
          <div className="info-box">Nova senha no proprio navegador</div>
        </div>

        <form className="login-right" onSubmit={handleSubmit}>
          <h2>Redefinir senha</h2>
          <p>Use o token recebido por e-mail para criar uma nova senha.</p>

          <input
            type="text"
            value={form.token}
            onChange={(event) => updateField('token', event.target.value)}
            placeholder="Token de recuperacao"
            aria-label="Token de recuperacao"
            required
          />

          <input
            type="password"
            value={form.senha}
            onChange={(event) => updateField('senha', event.target.value)}
            placeholder="Nova senha"
            aria-label="Nova senha"
            required
          />

          <input
            type="password"
            value={form.confirmarSenha}
            onChange={(event) => updateField('confirmarSenha', event.target.value)}
            placeholder="Confirmar nova senha"
            aria-label="Confirmar nova senha"
            required
          />

          {error && <div className="feedback error">{error}</div>}
          {success && <div className="feedback success">{success}</div>}

          <button type="submit" disabled={loading}>
            {loading ? 'Redefinindo...' : 'Trocar senha'}
          </button>
        </form>
      </section>
    </main>
  );
}

>>>>>>> 4bd12d3 (Atualização p deploy vercel)
function DashboardPage({ onNavigate, user, onLogout }) {
  const [monitoramentos, setMonitoramentos] = useState([]);
  const [newMonitoramento, setNewMonitoramento] = useState(emptyMonitoramento);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');

  const metrics = useMemo(
    () => [
      { label: 'Processos Ativos', value: monitoramentos.length },
      { label: 'Audiencias Hoje', value: 0 },
      { label: 'Prazos Urgentes', value: 0 },
      {
        label: 'Consultados',
        value: monitoramentos.filter((item) => item.ultimaConsulta).length,
      },
    ],
    [monitoramentos],
  );

  async function loadMonitoramentos() {
    setLoading(true);
    setError('');

    try {
      const data = await api.listMonitoramentos();
      setMonitoramentos(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(
        err.message ||
          'Nao foi possivel carregar os monitoramentos do backend.',
      );
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadMonitoramentos();
  }, []);

  function updateMonitoramento(field, value) {
    setNewMonitoramento((current) => ({ ...current, [field]: value }));
  }

  async function createMonitoramento(event) {
    event.preventDefault();
    setSaving(true);
    setError('');

    try {
      const created = await api.createMonitoramento(newMonitoramento);
      setMonitoramentos((current) => [created, ...current]);
      setNewMonitoramento(emptyMonitoramento);
    } catch (err) {
      setError(err.message || 'Nao foi possivel cadastrar o monitoramento.');
    } finally {
      setSaving(false);
    }
  }

  return (
    <main className="dashboard-page">
<<<<<<< HEAD
      <aside className="sidebar">
        <h2>JusRadar</h2>

        <ul className="menu">
          {menuItems.map((item, index) => (
            <li key={`${item.label}-${index}`}>
              <button type="button" onClick={() => onNavigate(item.id)}>
                {item.label}
              </button>
            </li>
          ))}
          <li>
            <button type="button" onClick={onLogout}>
              Sair
            </button>
          </li>
        </ul>
      </aside>
=======
      <Sidebar
        active="dashboard"
        counts={{
          processos: monitoramentos.length,
          audiencias: 0,
        }}
        onNavigate={onNavigate}
        onLogout={onLogout}
        user={user}
      />
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

      <section className="dashboard-main">
        <div className="topbar">
          <h1>Dashboard Juridico</h1>

          <div className="dashboard-user">
            Advogado: {user?.email || 'Usuario conectado'}
          </div>
        </div>

        {error && <div className="feedback error">{error}</div>}

        <div className="cards">
          {metrics.map((metric) => (
            <article className="card" key={metric.label}>
              <h3>{metric.label}</h3>
              <p>{metric.value}</p>
            </article>
          ))}
        </div>

        <form className="monitoring-form" onSubmit={createMonitoramento}>
          <input
            type="text"
            value={newMonitoramento.numeroProcesso}
            onChange={(event) =>
              updateMonitoramento('numeroProcesso', event.target.value)
            }
            placeholder="Numero do processo"
            required
          />
          <input
            type="text"
            value={newMonitoramento.tribunal}
            onChange={(event) => updateMonitoramento('tribunal', event.target.value)}
            placeholder="Tribunal"
            required
          />
          <input
            type="text"
            value={newMonitoramento.documentoCliente}
            onChange={(event) =>
              updateMonitoramento('documentoCliente', event.target.value)
            }
            placeholder="Documento do cliente"
            required
          />
<<<<<<< HEAD
=======
          <input
            type="text"
            value={newMonitoramento.nomeCliente}
            onChange={(event) =>
              updateMonitoramento('nomeCliente', event.target.value)
            }
            placeholder="Nome do cliente"
          />
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
          <button type="submit" disabled={saving}>
            {saving ? 'Salvando...' : 'Adicionar'}
          </button>
        </form>

        <div className="table-container">
          <h2>Processos Monitorados</h2>

          <div className="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>Processo</th>
                  <th>Cliente</th>
<<<<<<< HEAD
=======
                  <th>Documento</th>
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
                  <th>Status</th>
                  <th>Ultima Atualizacao</th>
                </tr>
              </thead>

              <tbody>
                {loading ? (
                  <tr>
<<<<<<< HEAD
                    <td colSpan="4">Carregando dados do backend...</td>
                  </tr>
                ) : monitoramentos.length === 0 ? (
                  <tr>
                    <td colSpan="4">Nenhum processo monitorado ainda.</td>
=======
                    <td colSpan="5">Carregando dados do backend...</td>
                  </tr>
                ) : monitoramentos.length === 0 ? (
                  <tr>
                    <td colSpan="5">Nenhum processo monitorado ainda.</td>
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
                  </tr>
                ) : (
                  monitoramentos.map((item) => {
                    const status = getStatus(item);

                    return (
                      <tr key={item.id || item.numeroProcesso}>
                        <td>{item.numeroProcesso}</td>
                        <td>{getClientName(item)}</td>
<<<<<<< HEAD
=======
                        <td>{getClientDocument(item)}</td>
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
                        <td>
                          <span className={`status ${status.className}`}>
                            {status.label}
                          </span>
                        </td>
                        <td>{formatDate(item.ultimaConsulta || item.criadoEm)}</td>
                      </tr>
                    );
                  })
                )}
              </tbody>
            </table>
          </div>
        </div>
      </section>
    </main>
  );
}

<<<<<<< HEAD
function ProcessosPage({ onNavigate }) {
  const [form, setForm] = useState({
    documento: '',
    tribunal: 'TJPI',
=======
function ProcessosPage({ onNavigate, onLogout, user }) {
  const [form, setForm] = useState({
    documento: '',
    tribunal: 'TJPI',
    nomeCliente: '',
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
    documentoCliente: '',
  });
  const [resultado, setResultado] = useState(null);
  const [loading, setLoading] = useState(false);
  const [savingProcesso, setSavingProcesso] = useState('');
  const [feedback, setFeedback] = useState('');
  const [error, setError] = useState('');

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function consultar(event) {
    event.preventDefault();
    setLoading(true);
    setError('');
    setFeedback('');

    try {
      const data = await api.consultarProcessos({
        documento: form.documento,
        tribunal: form.tribunal,
      });

      setResultado(data);
    } catch (err) {
      setResultado(null);
      setError(err.message || 'Nao foi possivel consultar a API REST.');
    } finally {
      setLoading(false);
    }
  }

  async function monitorar(processo) {
    const numeroProcesso = processo.numero || form.documento;
    setSavingProcesso(numeroProcesso);
    setError('');
    setFeedback('');

    try {
      await api.createMonitoramento({
        numeroProcesso,
        tribunal: processo.tribunal || form.tribunal,
<<<<<<< HEAD
=======
        nomeCliente: form.nomeCliente,
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
        documentoCliente: form.documentoCliente || form.documento,
      });

      setFeedback('Processo adicionado ao monitoramento.');
    } catch (err) {
      setError(err.message || 'Nao foi possivel monitorar este processo.');
    } finally {
      setSavingProcesso('');
    }
  }

  const processos = resultado?.processos || [];

  return (
    <main className="process-page">
<<<<<<< HEAD
      <aside className="sidebar">
        <h2>JusRadar</h2>

        <ul className="menu">
          {menuItems.map((item, index) => (
            <li key={`${item.label}-${index}`}>
              <button type="button" onClick={() => onNavigate(item.id)}>
                {item.label}
              </button>
            </li>
          ))}
        </ul>
      </aside>
=======
      <Sidebar
        active="processos"
        onNavigate={onNavigate}
        onLogout={onLogout}
        user={user}
      />
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

      <section className="process-main">
        <div className="topbar">
          <h1>Consulta de Processos</h1>
          <button type="button" className="secondary-button" onClick={() => onNavigate('dashboard')}>
            Voltar
          </button>
        </div>

        {error && <div className="feedback error">{error}</div>}
        {feedback && <div className="feedback success">{feedback}</div>}

        <form className="process-form" onSubmit={consultar}>
          <input
            type="text"
            value={form.documento}
            onChange={(event) => updateField('documento', event.target.value)}
            placeholder="Numero do processo"
            required
          />
          <input
            type="text"
            value={form.tribunal}
            onChange={(event) => updateField('tribunal', event.target.value)}
            placeholder="Tribunal"
            required
          />
          <input
            type="text"
            value={form.documentoCliente}
            onChange={(event) => updateField('documentoCliente', event.target.value)}
            placeholder="Documento do cliente"
          />
<<<<<<< HEAD
=======
          <input
            type="text"
            value={form.nomeCliente}
            onChange={(event) => updateField('nomeCliente', event.target.value)}
            placeholder="Nome do cliente"
          />
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
          <button type="submit" disabled={loading}>
            {loading ? 'Consultando...' : 'Consultar'}
          </button>
        </form>

        <div className="table-container">
          <h2>
            {resultado
              ? `${resultado.totalEncontrados} processo(s) encontrado(s)`
              : 'Resultados da consulta'}
          </h2>

          <div className="table-scroll">
            <table>
              <thead>
                <tr>
                  <th>Processo</th>
                  <th>Classe</th>
                  <th>Orgao julgador</th>
                  <th>Ultima movimentacao</th>
                  <th>Acao</th>
                </tr>
              </thead>

              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan="5">Consultando DataJud pelo backend...</td>
                  </tr>
                ) : processos.length === 0 ? (
                  <tr>
                    <td colSpan="5">Informe um processo para consultar.</td>
                  </tr>
                ) : (
<<<<<<< HEAD
                  processos.map((processo) => (
                    <tr key={processo.numero}>
=======
                  processos.map((processo, index) => (
                    <tr key={`${processo.numero}-${processo.orgaoJulgador || 'sem-orgao'}-${index}`}>
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
                      <td>{processo.numero}</td>
                      <td>{processo.classe || '-'}</td>
                      <td>{processo.orgaoJulgador || '-'}</td>
                      <td>{processo.situacao || processo.dataUltimaMovimentacao || '-'}</td>
                      <td>
                        <button
                          type="button"
                          className="table-button"
                          disabled={savingProcesso === processo.numero}
                          onClick={() => monitorar(processo)}
                        >
                          {savingProcesso === processo.numero ? 'Salvando...' : 'Monitorar'}
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </section>
    </main>
  );
}

function ChatPage({ onNavigate }) {
  const [form, setForm] = useState({
    numeroProcesso: '0000000-00',
    tribunal: 'TJPI',
    pergunta: '',
  });
  const [loading, setLoading] = useState(false);
  const [messages, setMessages] = useState([
    {
      id: 1,
      sender: 'bot',
      text: 'Ola advogado! Envie um processo, tribunal e pergunta para analisar no backend.',
    },
  ]);
  const messagesRef = useRef(null);

  useEffect(() => {
    if (messagesRef.current) {
      messagesRef.current.scrollTop = messagesRef.current.scrollHeight;
    }
  }, [messages]);

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function sendMessage(event) {
    event.preventDefault();

    if (!form.pergunta.trim()) {
      return;
    }

    const now = Date.now();
    const question = form.pergunta.trim();

    setMessages((currentMessages) => [
      ...currentMessages,
      { id: now, sender: 'user', text: question },
    ]);
    setLoading(true);

    try {
      const user = getStoredUser();
      const displayName =
        localStorage.getItem(USER_NAME_KEY) || user?.email || 'advogado';
      const perguntaComTratamento = `${question}\n\nAo responder, chame o usuario por \"${displayName}\".`;

      const response = await api.analisarProcesso({
        numeroProcesso: form.numeroProcesso,
        tribunal: form.tribunal,
        pergunta: perguntaComTratamento,
      });

      setMessages((currentMessages) => [
        ...currentMessages,
        {
          id: now + 1,
          sender: 'bot',
          text: response.analise || 'Analise concluida pelo backend.',
        },
      ]);
      updateField('pergunta', '');
    } catch (err) {
      setMessages((currentMessages) => [
        ...currentMessages,
        {
          id: now + 1,
          sender: 'bot',
          text:
            err.message ||
            'Nao consegui consultar o backend agora. Verifique se a API esta rodando.',
        },
      ]);
    } finally {
      setLoading(false);
    }
  }

  return (
    <main className="chat-page">
      <section className="chat-container">
        <header className="chat-header">
          <button type="button" className="back-button" onClick={() => onNavigate('dashboard')}>
            Dashboard
          </button>
          <h2>JusRadar IA</h2>
          <p>Assistente Juridico Inteligente</p>
        </header>

        <div className="chat-messages" ref={messagesRef}>
          {messages.map((message) => (
            <div className={`message ${message.sender}`} key={message.id}>
              {message.text}
            </div>
          ))}
          {loading && <div className="message bot">Analisando no backend...</div>}
        </div>

        <form className="chat-input chat-input-connected" onSubmit={sendMessage}>
          <div className="chat-fields">
            <input
              type="text"
              value={form.tribunal}
              onChange={(event) => updateField('tribunal', event.target.value)}
              placeholder="Tribunal"
              aria-label="Tribunal"
              required
            />
            <input
              type="text"
              value={form.pergunta}
              onChange={(event) => updateField('pergunta', event.target.value)}
              placeholder="Digite sua pergunta..."
              aria-label="Digite sua pergunta"
              required
            />
          </div>

          <button type="submit" disabled={loading}>
            {loading ? 'Enviando...' : 'Enviar'}
          </button>
        </form>
      </section>
    </main>
  );
}

<<<<<<< HEAD
function DocumentosPage({ onNavigate }) {
=======
function DocumentosPage({ onNavigate, onLogout, user }) {
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
  const [form, setForm] = useState(emptyDocumento);
  const [loadingFormat, setLoadingFormat] = useState('');
  const [error, setError] = useState('');

  function updateField(field, value) {
    setForm((current) => ({ ...current, [field]: value }));
  }

  async function handleDownload(formato) {
    setLoadingFormat(formato);
    setError('');

    try {
      const { blob, fileName } = await api.gerarDocumento(form, formato);
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');

      link.href = url;
      link.download = fileName || `documento.${formato}`;
      document.body.appendChild(link);
      link.click();
      link.remove();
      URL.revokeObjectURL(url);
    } catch (err) {
      setError(err.message || 'Nao foi possivel baixar o documento.');
    } finally {
      setLoadingFormat('');
    }
  }

  return (
    <main className="document-page">
<<<<<<< HEAD
      <aside className="sidebar">
        <h2>JusRadar</h2>

        <ul className="menu">
          {menuItems.map((item, index) => (
            <li key={`${item.label}-${index}`}>
              <button type="button" onClick={() => onNavigate(item.id)}>
                {item.label}
              </button>
            </li>
          ))}
        </ul>
      </aside>
=======
      <Sidebar
        active="documentos"
        onNavigate={onNavigate}
        onLogout={onLogout}
        user={user}
      />
>>>>>>> 4bd12d3 (Atualização p deploy vercel)

      <section className="document-main">
        <div className="topbar">
          <h1>Download de Documentos</h1>
          <button type="button" className="secondary-button" onClick={() => onNavigate('dashboard')}>
            Voltar
          </button>
        </div>

        {error && <div className="feedback error">{error}</div>}

        <form className="document-form" onSubmit={(event) => event.preventDefault()}>
          <input
            type="text"
            value={form.numeroProcesso}
            onChange={(event) => updateField('numeroProcesso', event.target.value)}
            placeholder="Numero do processo"
            required
          />
          <input
            type="text"
            value={form.tribunal}
            onChange={(event) => updateField('tribunal', event.target.value)}
            placeholder="Tribunal"
            required
          />
          <input
            type="text"
            value={form.nomeAdvogado}
            onChange={(event) => updateField('nomeAdvogado', event.target.value)}
            placeholder="Nome do advogado"
            required
          />
          <input
            type="text"
            value={form.oabNumero}
            onChange={(event) => updateField('oabNumero', event.target.value)}
            placeholder="OAB/UF e numero"
            required
          />
          <input
            type="text"
            value={form.nomeCliente}
            onChange={(event) => updateField('nomeCliente', event.target.value)}
            placeholder="Nome do cliente"
            required
          />
          <select
            value={form.tipoDocumento}
            onChange={(event) => updateField('tipoDocumento', event.target.value)}
            required
          >
            <option value="PETICAO_SIMPLES">Peticao simples</option>
            <option value="RECURSO">Recurso</option>
            <option value="HABEAS_CORPUS">Habeas corpus</option>
            <option value="MANIFESTACAO">Manifestacao</option>
            <option value="REQUERIMENTO_OAB">Requerimento OAB</option>
          </select>
          <textarea
            value={form.pergunta}
            onChange={(event) => updateField('pergunta', event.target.value)}
            placeholder="Contexto ou pedido para compor o documento"
            rows="6"
          />

          <div className="document-actions">
            <button
              type="button"
              disabled={Boolean(loadingFormat)}
              onClick={() => handleDownload('pdf')}
            >
              {loadingFormat === 'pdf' ? 'Gerando PDF...' : 'Baixar PDF'}
            </button>
            <button
              type="button"
              disabled={Boolean(loadingFormat)}
              onClick={() => handleDownload('docx')}
            >
              {loadingFormat === 'docx' ? 'Gerando DOCX...' : 'Baixar DOCX'}
            </button>
          </div>
        </form>
      </section>
    </main>
  );
}

export default function App() {
<<<<<<< HEAD
  const [page, setPage] = useState('login');
=======
  const [page, setPage] = useState(getInitialRoute);
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
  const [user, setUser] = useState(() => getStoredUser());

  useEffect(() => {
    if (user) {
      setPage('dashboard');
    }
  }, [user]);

  function handleLogout() {
    removeToken();
    localStorage.removeItem(USER_NAME_KEY);
    setUser(null);
    setPage('login');
  }

  if (page === 'dashboard' && user) {
    return (
      <DashboardPage
        onNavigate={setPage}
        onLogout={handleLogout}
        user={user}
      />
    );
  }

  if (page === 'chat' && user) {
    return <ChatPage onNavigate={setPage} />;
  }

  if (page === 'processos' && user) {
<<<<<<< HEAD
    return <ProcessosPage onNavigate={setPage} />;
  }

  if (page === 'documentos' && user) {
    return <DocumentosPage onNavigate={setPage} />;
=======
    return <ProcessosPage onNavigate={setPage} onLogout={handleLogout} user={user} />;
  }

  if (page === 'documentos' && user) {
    return <DocumentosPage onNavigate={setPage} onLogout={handleLogout} user={user} />;
  }

  if (page === 'reset-password') {
    return <ResetPasswordPage />;
>>>>>>> 4bd12d3 (Atualização p deploy vercel)
  }

  return <LoginPage onAuthenticated={setUser} />;
}
