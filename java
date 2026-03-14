// ============================================
// RG DIGITAL SP - JavaScript Principal
// ============================================

// Estado da aplicação
const AppState = {
    currentScreen: 'login',
    passwordVisible: false,
    passwordValue: ''
};

// ============================================
// INICIALIZAÇÃO
// ============================================

document.addEventListener('DOMContentLoaded', () => {
    // Mostra a tela de loading por 3 segundos
    const loadingScreen = document.getElementById('loading-screen');
    const loginScreen = document.getElementById('login-screen');

    // Esconde a tela de login inicialmente
    if (loginScreen) {
        loginScreen.classList.remove('active');
    }

    // Após 1 segundo, esconde a tela de loading e mostra a tela de login
    setTimeout(() => {
        if (loadingScreen) {
            loadingScreen.classList.add('hidden');
            // Remove completamente após a animação
            setTimeout(() => {
                loadingScreen.style.display = 'none';
            }, 500);
        }

        // Mostra a tela de login
        if (loginScreen) {
            loginScreen.classList.add('active');
        }

        // Inicializa o app
        initializeApp();
    }, 1000);
});

function initializeApp() {
    setupPasswordToggle();
    setupPasswordInput();
    setupLoginButton();
    setupTabNavigation();
    setupServiceItems();
    setupOptionItems();
    setupAddWalletButton();
    setupRGDetailScreen();

    // Garante que está na tela de login
    showScreen('login');
}

// ============================================
// NAVEGAÇÃO ENTRE TELAS
// ============================================

function showScreen(screenName) {
    // Oculta todas as telas
    const screens = document.querySelectorAll('.screen');
    screens.forEach(screen => {
        screen.classList.remove('active');
    });

    // Mostra a tela selecionada
    const targetScreen = document.getElementById(`${screenName}-screen`);
    if (targetScreen) {
        targetScreen.classList.add('active');
        AppState.currentScreen = screenName;

        // Atualiza a tab bar ativa
        updateActiveTab(screenName);
    }
}

function updateActiveTab(screenName) {
    const tabItems = document.querySelectorAll('.tab-item');
    // Map logical screen to the tab that should be active
    const activeTabForScreen = {
        wallet: 'wallet',
        services: 'services',
        options: 'options',
        'rg-detail': 'wallet',
    };
    const targetTab = activeTabForScreen[screenName];
    tabItems.forEach((tab) => {
        tab.classList.remove('active');
        if (targetTab && tab.dataset.screen === targetTab) {
            tab.classList.add('active');
        }
    });
}

// ============================================
// TAB NAVIGATION
// ============================================

function setupTabNavigation() {
    const tabItems = document.querySelectorAll('.tab-item');

    tabItems.forEach(tab => {
        tab.addEventListener('click', (e) => {
            e.preventDefault();
            const targetScreen = tab.dataset.screen;

            // Não permite navegar para login pela tab bar
            if (targetScreen && targetScreen !== 'login') {
                showScreen(targetScreen);

                // Feedback tátil
                tab.style.transform = 'scale(0.95)';
                setTimeout(() => {
                    tab.style.transform = '';
                }, 150);
            }
        });
    });
}

// ============================================
// TELA DE LOGIN - Toggle de Senha
// ============================================

function setupPasswordToggle() {
    const toggleButton = document.getElementById('toggle-password');
    const passwordInput = document.getElementById('password-input');

    if (!toggleButton || !passwordInput) return;

    toggleButton.addEventListener('click', () => {
        AppState.passwordVisible = !AppState.passwordVisible;

        if (AppState.passwordVisible) {
            passwordInput.type = 'text';
            toggleButton.setAttribute('aria-label', 'Ocultar senha');
            toggleButton.innerHTML = `
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19m-6.72-1.07a3 3 0 11-4.24-4.24"/>
                    <line x1="1" y1="1" x2="23" y2="23"/>
                </svg>
            `;
        } else {
            passwordInput.type = 'password';
            toggleButton.setAttribute('aria-label', 'Mostrar senha');
            toggleButton.innerHTML = `
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                    <circle cx="12" cy="12" r="3"/>
                </svg>
            `;
        }

        // Animação de fade
        toggleButton.style.opacity = '0.5';
        setTimeout(() => {
            toggleButton.style.opacity = '1';
        }, 150);

        // Foca de volta no input
        passwordInput.focus();
    });
}

// ============================================
// TELA DE LOGIN - Validação de Senha
// ============================================

function setupPasswordInput() {
    const passwordInput = document.getElementById('password-input');
    const loginButton = document.getElementById('login-button');

    if (!passwordInput || !loginButton) return;

    passwordInput.addEventListener('input', (e) => {
        AppState.passwordValue = e.target.value.trim();
        updateLoginButtonState();
    });

    // Valida ao perder o foco também
    passwordInput.addEventListener('blur', () => {
        updateLoginButtonState();
    });
}

function updateLoginButtonState() {
    const loginButton = document.getElementById('login-button');
    if (!loginButton) return;

    const hasPassword = AppState.passwordValue.length >= 5;

    if (hasPassword) {
        loginButton.disabled = false;
        loginButton.style.cursor = 'pointer';
    } else {
        loginButton.disabled = true;
        loginButton.style.cursor = 'not-allowed';
    }
}

// ============================================
// TELA DE LOGIN - Botão Entrar
// ============================================

function setupLoginButton() {
    const loginButton = document.getElementById('login-button');

    if (!loginButton) return;

    loginButton.addEventListener('click', async () => {
        if (loginButton.disabled) return;

        const passwordInput = document.getElementById('password-input');
        const password = passwordInput.value.trim();

        if (!password) {
            showToast('Por favor, insira sua senha');
            return;
        }

        // Simula verificação de login
        loginButton.disabled = true;
        loginButton.innerHTML = '<span class="spinner"></span> Entrando...';
        loginButton.style.opacity = '0.7';

        // Simula delay de API
        await simulateLogin(password);

        // Após login bem-sucedido, navega para a tela Carteira
        setTimeout(() => {
            showScreen('wallet');
            loginButton.innerHTML = 'Entrar';
            loginButton.disabled = false;
            loginButton.style.opacity = '1';
            passwordInput.value = '';
            AppState.passwordValue = '';
            updateLoginButtonState();
        }, 1500);
    });
}

async function simulateLogin(password) {
    return new Promise((resolve) => {
        setTimeout(() => {
            // Simula sucesso (em produção, aqui seria a chamada à API)
            console.log('Login simulado para senha:', password ? '***' : 'vazia');
            resolve({
                success: true
            });
        }, 1200);
    });
}

// ============================================
// TELA SERVIÇOS - Itens Clicáveis
// ============================================

function setupServiceItems() {
    const serviceItems = document.querySelectorAll('.service-item');

    serviceItems.forEach(item => {
        item.addEventListener('click', () => {
            // O feedback visual é feito pelo CSS :active
            // Simula navegação para detalhe do serviço
            const serviceTitle = item.querySelector('.service-title').textContent;
            showToast(`Abrindo: ${serviceTitle}`);

            // Em produção, aqui abriria a tela de detalhe ou modal
            // showServiceDetail(serviceTitle);
        });
    });
}

// ============================================
// TELA OPÇÕES - Itens Clicáveis
// ============================================

function setupOptionItems() {
    const optionItems = document.querySelectorAll('.option-item');

    optionItems.forEach(item => {
        item.addEventListener('click', () => {
            // Feedback visual
            item.style.transform = 'translateX(4px)';
            item.style.backgroundColor = 'var(--color-bg-light)';

            setTimeout(() => {
                item.style.transform = '';
                item.style.backgroundColor = '';
            }, 200);

            // Simula ação
            const optionTitle = item.querySelector('.option-title').textContent;
            showToast(`Abrindo: ${optionTitle}`);

            // Em produção, cada opção teria sua ação específica
            handleOptionClick(optionTitle);
        });
    });
}

function handleOptionClick(optionTitle) {
    // Simula diferentes ações para cada opção
    const actions = {
        'Alteração de senha': () => showToast('Redirecionando para alteração de senha...'),
        'Acesso com biometria': () => showToast('Abrindo configurações de biometria...'),
        'Perguntas frequentes': () => showToast('Carregando FAQ...'),
        'Chat de atendimento': () => showToast('Iniciando chat...'),
        'Termos e condições de uso': () => showToast('Abrindo termos e condições...'),
        'Política de privacidade': () => showToast('Abrindo política de privacidade...')
    };

    if (actions[optionTitle]) {
        actions[optionTitle]();
    }
}

// ============================================
// TELA CARTEIRA - Botão Adicionar
// ============================================

function setupAddWalletButton() {
    const addWalletButton = document.getElementById('add-wallet-button');

    if (!addWalletButton) return;

    addWalletButton.addEventListener('click', () => {
        // Feedback visual
        addWalletButton.style.transform = 'scale(0.98)';
        addWalletButton.style.backgroundColor = 'rgba(11, 75, 90, 0.05)';

        setTimeout(() => {
            addWalletButton.style.transform = '';
            addWalletButton.style.backgroundColor = '';
        }, 150);

        // Simula ação de adicionar carteira
        showToast('Abrindo fluxo de adição de carteira...');

        // Em produção, aqui abriria o formulário/modal de adição
        // showAddWalletModal();
    });
}

// ============================================
// MODAL DA CARTEIRA
// ============================================

function setupRGDetailScreen() {
    const rgCard = document.querySelector('.rg-card');
    const backButton = document.querySelector('[data-rg-back]');
    const tabButtons = document.querySelectorAll('.rg-detail-tabs .rg-tab-button');
    const panels = document.querySelectorAll('.rg-detail-panel');

    if (!rgCard || !backButton) return;

    const setActivePanel = (target) => {
        tabButtons.forEach((button) => {
            button.classList.toggle('active', button.dataset.rgTab === target);
        });
        panels.forEach((panel) => {
            panel.classList.toggle('active', panel.dataset.rgPanel === target);
        });
    };

    rgCard.addEventListener('click', () => {
        showScreen('rg-detail');
        setActivePanel('front');
    });

    backButton.addEventListener('click', () => {
        showScreen('wallet');
    });

    tabButtons.forEach((button) => {
        button.addEventListener('click', () => {
            const target = button.dataset.rgTab;
            if (target) {
                setActivePanel(target);
            }
        });
    });
}

// ============================================
// LINKS E NAVEGAÇÃO SECUNDÁRIA
// ============================================

// Link "Esqueci minha senha"
document.addEventListener('DOMContentLoaded', () => {
    const forgotPasswordLink = document.querySelector('.forgot-password-link');
    if (forgotPasswordLink) {
        forgotPasswordLink.addEventListener('click', (e) => {
            e.preventDefault();
            showToast('Redirecionando para recuperação de senha...');
            // Em produção: window.location.href = '/recuperar-senha';
        });
    }

    // Links do footer (Ajuda, Privacidade, Termos)
    const footerLinks = document.querySelectorAll('.footer-link');
    footerLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const linkText = link.textContent.trim();
            showToast(`Abrindo: ${linkText}`);
            // Em produção, cada link abriria sua respectiva página/modal
        });
    });
});

// ============================================
// UTILITÁRIOS - Toast Notifications
// ============================================

function showToast(message, duration = 2000) {
    // Remove toast existente se houver
    const existingToast = document.querySelector('.toast');
    if (existingToast) {
        existingToast.remove();
    }

    // Cria novo toast
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = message;
    toast.style.cssText = `
        position: fixed;
        bottom: 100px;
        left: 50%;
        transform: translateX(-50%);
        background-color: rgba(0, 0, 0, 0.8);
        color: white;
        padding: 12px 24px;
        border-radius: 8px;
        font-size: 14px;
        z-index: 1000;
        animation: toastSlideIn 0.3s ease;
        max-width: 80%;
        text-align: center;
    `;

    // Adiciona animação CSS
    if (!document.querySelector('#toast-styles')) {
        const style = document.createElement('style');
        style.id = 'toast-styles';
        style.textContent = `
            @keyframes toastSlideIn {
                from {
                    opacity: 0;
                    transform: translateX(-50%) translateY(20px);
                }
                to {
                    opacity: 1;
                    transform: translateX(-50%) translateY(0);
                }
            }
            @keyframes toastSlideOut {
                from {
                    opacity: 1;
                    transform: translateX(-50%) translateY(0);
                }
                to {
                    opacity: 0;
                    transform: translateX(-50%) translateY(20px);
                }
            }
        `;
        document.head.appendChild(style);
    }

    document.body.appendChild(toast);

    // Remove após duração
    setTimeout(() => {
        toast.style.animation = 'toastSlideOut 0.3s ease';
        setTimeout(() => {
            toast.remove();
        }, 300);
    }, duration);
}

// ============================================
// PREVENÇÃO DE COMPORTAMENTOS PADRÃO
// ============================================

// Previne scroll em elementos não-scrolláveis
document.addEventListener('touchmove', (e) => {
    const target = e.target;
    const isScrollable = target.closest('.screen-content, .login-container');

    if (!isScrollable && target.closest('.screen')) {
        // Permite scroll apenas em áreas específicas
    }
}, {
    passive: true
});

// Previne zoom em double-tap (opcional)
let lastTouchEnd = 0;
document.addEventListener('touchend', (e) => {
    const now = Date.now();
    if (now - lastTouchEnd <= 300) {
        e.preventDefault();
    }
    lastTouchEnd = now;
}, false);

// ============================================
// ACESSIBILIDADE
// ============================================

// Adiciona labels de acessibilidade
document.addEventListener('DOMContentLoaded', () => {
    // Tab items
    const tabItems = document.querySelectorAll('.tab-item');
    tabItems.forEach(tab => {
        const label = tab.querySelector('.tab-label').textContent;
        tab.setAttribute('aria-label', `Navegar para ${label}`);
        tab.setAttribute('role', 'button');
    });

    // Service items
    const serviceItems = document.querySelectorAll('.service-item');
    serviceItems.forEach(item => {
        const title = item.querySelector('.service-title').textContent;
        item.setAttribute('aria-label', `Abrir ${title}`);
        item.setAttribute('role', 'button');
    });

    // Option items
    const optionItems = document.querySelectorAll('.option-item');
    optionItems.forEach(item => {
        const title = item.querySelector('.option-title').textContent;
        item.setAttribute('aria-label', `Abrir ${title}`);
        item.setAttribute('role', 'button');
    });

    // Botões
    const buttons = document.querySelectorAll('button');
    buttons.forEach(button => {
        if (!button.getAttribute('aria-label') && button.textContent) {
            button.setAttribute('aria-label', button.textContent.trim());
        }
    });
});

// ============================================
// EXPORT (para uso em módulos, se necessário)
// ============================================

if (typeof module !== 'undefined' && module.exports) {
    module.exports = {
        showScreen,
        showToast,
        AppState
    };
}

const swipeContent = document.querySelector(".swipe-content");

let startX = 0;
let currentX = 0;
let currentIndex = 0;

let isDragging = false;

const containerWidth =
document.querySelector(".swipe-container").offsetWidth;


// Move com animação suave
function animateTo(index){

    swipeContent.style.transition =
    "transform 0.35s cubic-bezier(0.22, 1, 0.36, 1)";

    swipeContent.style.transform =
    `translateX(-${index * containerWidth}px)`;

}



// Move em tempo real com dedo
function moveDrag(deltaX){

    swipeContent.style.transition = "none";

    const base =
    -currentIndex * containerWidth;

    swipeContent.style.transform =
    `translateX(${base + deltaX}px)`;

}



// Quando começa o toque
swipeContent.addEventListener("touchstart",(e)=>{

    startX = e.touches[0].clientX;

    isDragging = true;

});



// Quando arrasta
swipeContent.addEventListener("touchmove",(e)=>{

    if(!isDragging) return;

    currentX = e.touches[0].clientX;

    const deltaX =
    currentX - startX;

    moveDrag(deltaX);

});



// Quando solta
swipeContent.addEventListener("touchend",()=>{

    const deltaX =
    currentX - startX;

    if(deltaX < -50){

        currentIndex++;

    }
    else if(deltaX > 50){

        currentIndex--;

    }

    animateTo(currentIndex);

    isDragging = false;

});