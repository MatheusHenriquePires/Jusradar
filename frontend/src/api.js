const API_BASE_URL = '/api/v1';
const TOKEN_KEY = 'jusradar_token';

function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function saveToken(token) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function removeToken() {
  localStorage.removeItem(TOKEN_KEY);
}

export function getStoredUser() {
  const token = getToken();

  if (!token) {
    return null;
  }

  try {
    const payload = JSON.parse(atob(token.split('.')[1]));

    return {
      email: payload.sub,
      advogadoId: payload.advogadoId,
      role: payload.role,
    };
  } catch {
    return null;
  }
}

async function request(path, options = {}) {
  const token = getToken();
  const headers = {
    ...(options.body ? { 'Content-Type': 'application/json' } : {}),
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers,
  });

  const contentType = response.headers.get('content-type') || '';
  const payload = contentType.includes('application/json')
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message =
      typeof payload === 'string' && payload
        ? payload
        : 'Nao foi possivel completar a solicitacao.';

    throw new Error(message);
  }

  return payload;
}

async function download(path, data) {
  const token = getToken();
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || 'Nao foi possivel gerar o documento.');
  }

  const blob = await response.blob();
  const disposition = response.headers.get('content-disposition') || '';
  const fileNameMatch = disposition.match(/filename="?([^"]+)"?/i);

  return {
    blob,
    fileName: fileNameMatch?.[1] || 'documento',
  };
}

export const api = {
  login: (data) =>
    request('/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  register: (data) =>
    request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  listMonitoramentos: () => request('/monitoramentos'),

  createMonitoramento: (data) =>
    request('/monitoramentos', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  analisarProcesso: (data) =>
    request('/assistente/analisar', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  gerarDocumento: (data, formato) =>
    download(`/assistente/gerar-documento?formato=${formato}`, data),
};
