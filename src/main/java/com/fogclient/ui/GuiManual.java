package com.fogclient.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

public class GuiManual extends GuiScreen {
   private List<String> rawLines = new ArrayList();
   private List<String> displayLines = new ArrayList();
   private int scroll = 0;
   private int maxScroll = 0;

   public GuiManual() {
      this.rawLines.add("Â§aÂ§lFogClient - Manual do UsuÃ¡rio");
      this.rawLines.add("");
      this.rawLines.add("Bem-vindo ao FogClient, um cliente focado em PvP, utilitÃ¡rios e otimizaÃ§Ã£o.");
      this.rawLines.add("Este manual detalha cada mÃ³dulo, suas configuraÃ§Ãµes e funcionamento.");
      this.rawLines.add("");
      this.rawLines.add("Â§2Â§lCOMO NAVEGAR");
      this.rawLines.add("- Â§fMenu Principal (ClickGUI): Pressione Â§aRSHIFTÂ§f (Tecla padrÃ£o, porÃ©m editÃ¡vel).");
      this.rawLines.add("- Â§fBotÃ£o Esquerdo: Liga/Desliga mÃ³dulos.");
      this.rawLines.add("- Â§fBotÃ£o Direito: Abre configuraÃ§Ãµes detalhadas ao clicar em cima de um mÃ³dulo.");
      this.rawLines.add("- Â§fBind: Serve para ligar e desligar o mÃ³dulo, clique na opÃ§Ã£o 'Bind' e aperte uma tecla para criar atalho.");
      this.rawLines.add("- Â§fAction: Serve executar a aÃ§Ã£o do mÃ³dulo quando ele jÃ¡ estiver ligado (diferente de bind), clique na opÃ§Ã£o 'Action' e aperte uma tecla para executar uma aÃ§Ã£o de um mÃ³dulo que tem esse recurso.");
      this.rawLines.add("");
      this.rawLines.add("Â§2Â§lCATEGORIAS E MÃ“DULOS");
      this.rawLines.add("");
      this.rawLines.add("Â§a[Guerra]");
      this.rawLines.add("Â§fEmpurraTudo:");
      this.rawLines.add("  Aumenta o knockback (empurrÃ£o) causado no inimigo.");
      this.rawLines.add("  SuperKB: ");
      this.rawLines.add("  - WTap + ResetSprint (Da mini resets no sprint durante o PvP e faz o Player ir mais para trÃ¡s).");
      this.rawLines.add("  - Packet (Envia pacotes para o servidor para simular um KnockBack mais forte no oponente)");
      this.rawLines.add("  - Legit (Ã‰ seguro e simula tÃ©cnica de \"W-Tap\" perfeita.)");
      this.rawLines.add("  PingSpoof: Quando vocÃª bate, o cliente segura esse pacote de ataque por alguns milissegundos (configurÃ¡vel no \"Ping Delay\", ex: 100ms) antes de enviar para o servidor.");
      this.rawLines.add("  Isso cria um efeito onde o inimigo recebe o dano e o empurrÃ£o com um leve atraso.");
      this.rawLines.add("");
      this.rawLines.add("Â§fFlechaCerteira:");
      this.rawLines.add("  Ao atirar uma flecha no oponente, a flecha faz a rota certa para onde o oponente estÃ¡ no momento do disparo.");
      this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
      this.rawLines.add("  - Silent: A mira nÃ£o mexe na sua tela.");
      this.rawLines.add("  - Predict: Calcula onde o inimigo estarÃ¡ (para alvos em movimento).");
      this.rawLines.add("  - Priority: Escolhe quem focar (Ex: Mais Perto).");
      this.rawLines.add("");
      this.rawLines.add("Â§fReachDiferenciado:");
      this.rawLines.add("  O ReachDiferenciado serve para vocÃª ganhar o PvP nos detalhes, dando aquele \"hit a mais\" que o inimigo nÃ£o alcanÃ§a, mas de forma que pareÃ§a natural (como se fosse combo ou ping). Se vocÃª quer bater de 6 blocos o tempo todo igual hack escancarado, esse mÃ³dulo nÃ£o vai fazer isso porque ele prioriza a seguranÃ§a da sua conta.");
      this.rawLines.add("  ");
      this.rawLines.add("  O mÃ³dulo manipula visualmente a posiÃ§Ã£o do jogador inimigo no seu cliente para validar o hit de longe:");
      this.rawLines.add("  1. Pra Frente (Perto de vocÃª): Quando o ReachDiferenciado ativa, ele puxa o inimigo para mais perto (teleporte invisÃ\u00advel ou rÃ¡pido) para o seu jogo entender que ele estÃ¡ no alcance do seu hit (3 blocos).");
      this.rawLines.add("  2. Pra TrÃ¡s (PosiÃ§Ã£o Real): Assim que vocÃª dÃ¡ o hit ou o mÃ³dulo desativa (pelas proteÃ§Ãµes de seguranÃ§a), o jogo \"corrige\" a posiÃ§Ã£o do inimigo devolvendo ele para onde o servidor diz que ele realmente estÃ¡.");
      this.rawLines.add(" ");
      this.rawLines.add("  Permite bater de mais longe que o normal.");
      this.rawLines.add("  Diferente de reachs comuns, ele utiliza randomizaÃ§Ã£o e heurÃ\u00adsticas (falhas propositais) para simular um comportamento humano e evitar detecÃ§Ã£o.");
      this.rawLines.add("  Ele Ã© projetado para ser Legit (indetectÃ¡vel) e simular \"sorte\" ou \"lag\" a seu favor.");
      this.rawLines.add("  Ele nÃ£o deixa vocÃª dar hits de longe muito rÃ¡pido seguidos (tem um delay mÃ\u00adnimo de 500ms entre hits de reach).");
      this.rawLines.add("  Se vocÃª der 2 hits seguidos de longe, ele forÃ§a o prÃ³ximo a ser normal para \"disfarÃ§ar\".");
      this.rawLines.add("  Se o servidor estiver lagado (TPS baixo), ele desativa para evitar bugs de movimentaÃ§Ã£o.");
      this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
      this.rawLines.add("  - Min/Max Reach: Define um intervalo (Ex: 3.0 a 4.0) para variar os hits.");
      this.rawLines.add("  - Chance %: Probabilidade do hit ter alcance aumentado.");
      this.rawLines.add("");
      this.rawLines.add("Â§fRecraftPerfeito:");
      this.rawLines.add("  Abre o inventÃ¡rio e faz sopas automaticamente, isso tudo acionado por uma tecla.");
      this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
      this.rawLines.add("  - Speed: Velocidade do recraft.");
      this.rawLines.add("");
      this.rawLines.add("Â§fDuploClique:");
      this.rawLines.add("  Configure outra tecla para tambÃ©m clicar com os botÃµes esquerdo ou direito.");
      this.rawLines.add("  AlÃ©m de depender apenas do click esquerdo do mouse para atacar, configure uma segunda tecla para fazer a mesma aÃ§Ã£o.");
      this.rawLines.add("  AlÃ©m de depender apenas do click direito do mouse para defender, comer ou colocar blocos, configure uma segunda tecla para fazer a mesma aÃ§Ã£o.");
      this.rawLines.add("");
      this.rawLines.add("Â§fAjudanteDeRecraft:");
      this.rawLines.add("  Move os itens do recraft automaticamente para a craft do inventÃ¡rio");
      this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
      this.rawLines.add("  - Speed: Rapidez do movimento.");
      this.rawLines.add("");
      this.rawLines.add("Â§fSopaPadrao:");
      this.rawLines.add("  VocÃª consegue tomar sopas como no HG em um mundo singleplayer.");
      this.rawLines.add("  Pode ser usado para tirar delays de sopas ou usar contra o BotPvP no Singleplayer e treinar.");
      this.rawLines.add("");
      this.rawLines.add("Â§fSemDefesa:");
      this.rawLines.add("  Bloqueia a defesa da espada.");
      this.rawLines.add("");
      this.rawLines.add("Â§fHitInfinito:");
      this.rawLines.add("  Remove o delay de hit da 1.8.");
      this.rawLines.add("  Permite clicar e combar extremamente rÃ¡pido.");
      this.rawLines.add("");
      this.rawLines.add("Â§fBlockHitPro:");
      this.rawLines.add("  Intercala defesa (block) no momento exato de troca de hits para reduzir dano e combar inimigos.");
      this.rawLines.add("");
      this.rawLines.add("Â§fMiraCerteira:");
      this.rawLines.add("  Ajuda a manter a mira no inimigo de forma suave.");
      this.rawLines.add("  Â§71. Hitbox Pixels (NÃ£o aumenta o hitbox do inimigo)");
      this.rawLines.add("  - Como funciona: Se vocÃª mirar um pouco fora do corpo do inimigo, o mÃ³dulo entende que vocÃª \"quase acertou\" e suavemente empurra sua mira para dentro do corpo dele.");
      this.rawLines.add("  - Na prÃ¡tica: Faz com que seja muito mais difÃ\u00adcil \"pinar\" (errar) a mira por poucos pixels. VocÃª pode mirar um pouco torto que ele corrige.");
      this.rawLines.add("  Â§72. Sensitivity Adjust (Muda a sensibilidade do mouse ao mirar)");
      this.rawLines.add("  - O que faz: Altera a sensibilidade do seu mouse automaticamente quando vocÃª passa a mira sobre um inimigo.");
      this.rawLines.add("  - Como funciona:");
      this.rawLines.add("    - Quando sua mira estÃ¡ em cima do inimigo: Ele diminui a sensibilidade (deixa o mouse mais lento/pesado). Isso ajuda vocÃª a nÃ£o passar a mira direto pelo alvo, dando mais precisÃ£o para manter o combo.");
      this.rawLines.add("    - Quando a mira sai do inimigo: A sensibilidade volta ao normal instantaneamente para vocÃª poder virar a cÃ¢mera rÃ¡pido.");
      this.rawLines.add("  - Na prÃ¡tica: Cria uma sensaÃ§Ã£o de \"imÃ£\" ou \"fricÃ§Ã£o\" quando a mira passa pelo oponente, facilitando manter a mira cravada nele durante o PvP..");
      this.rawLines.add("");
      this.rawLines.add("Â§fSopaFacil:");
      this.rawLines.add("  Ao apertar uma tecla configurÃ¡vel vocÃª toma uma sopa, dropa o pote e volta para a slot original.");
      this.rawLines.add("  VocÃª NÃƒO precisa ter o trabalho de ir atÃ© a sopa, tomar, dropar o pote e voltar para a espada, esse modulo faz essa funÃ§Ã£o automaticamente.");
      this.rawLines.add("");
      this.rawLines.add("Â§a[Player]");
      this.rawLines.add("Â§fRoubaTudo (Anti Traps na Obsidian):");
      this.rawLines.add("  Ao tentarem colocar lava em vocÃª ou ao seu redor (Raio configurÃ¡vel), ele pega automaticamente desde que vocÃª tenha baldes cheios ou vazios na hotbar (nÃ£o precisa estar com eles em mÃ£os).");
      this.rawLines.add("");
      this.rawLines.add("Â§fJuntaPotes:");
      this.rawLines.add("  Muito usado em HG, em casos de economia de potes ou organizaÃ§Ã£o de inventÃ¡rio.");
      this.rawLines.add("  - O que ele faz: Ele agrupa instantaneamente todos os potes (tigelas) espalhados pelo seu inventÃ¡rio em um Ãºnico pack.");
      this.rawLines.add("  - Como usar:");
      this.rawLines.add("  1. Abra seu inventÃ¡rio.");
      this.rawLines.add("  2. Passe o mouse sobre qualquer pote (tigela).");
      this.rawLines.add("  3. Clique com o BotÃ£o Lateral do Mouse (BotÃ£o 5, geralmente o de \"AvanÃ§ar\").");
      this.rawLines.add("");
      this.rawLines.add("Â§fLavaNoMeuPe:");
      this.rawLines.add("  Coloca lava nos seus pÃ©s e a recolhe rapidamente, evitando que o oponente roube a sua lava.");
      this.rawLines.add("  Essa estratÃ©gia Ã© muito usado em minimushs e gladiators, para ganhar VANTAGEM de KnockBack.");
      this.rawLines.add("");
      this.rawLines.add("Â§fHelperMLG:");
      this.rawLines.add("  Serve para ser indetectÃ¡vel e simular MLG legit e nunca morrer de queda.");
      this.rawLines.add("  - Como funciona:");
      this.rawLines.add("  1. VocÃª precisa estar segurando um Balde de Ã�gua na mÃ£o.");
      this.rawLines.add("  2. VocÃª deve estar olhando para o chÃ£o onde vai cair.");
      this.rawLines.add("  3. Quando vocÃª estiver a menos de 2 blocos de bater no chÃ£o, o mÃ³dulo clica extremamente rÃ¡pido (mais rÃ¡pido que reflexo humano) para colocar a Ã¡gua.");
      this.rawLines.add("  - LimitaÃ§Ã£o importante: Ele nÃ£o troca para o balde sozinho (vocÃª precisa jÃ¡ estar com ele na mÃ£o) e nÃ£o mira sozinho para baixo (vocÃª precisa olhar). Ele apenas executa o \"clique perfeito\" no tempo certo (o timing do MLG).");
      this.rawLines.add("");
      this.rawLines.add("Â§a[Mobilidade]");
      this.rawLines.add("Â§fPuloDoGato0KB:");
      this.rawLines.add("  Ã© um mÃ³dulo de movimentaÃ§Ã£o focado em reduzir o Knockback (empurrÃ£o) que vocÃª recebe ao tomar dano.");
      this.rawLines.add("  - O que ele faz: Ele faz seu personagem pular automaticamente no exato momento em que vocÃª toma um hit.");
      this.rawLines.add("  - Por que isso funciona (FÃ\u00adsica do Minecraft): No Minecraft, quando vocÃª estÃ¡ no ar (pulando), o atrito com o chÃ£o Ã© menor e a fÃ\u00adsica de \"empurrÃ£o\" funciona de forma diferente.");
      this.rawLines.add("  Ao pular no momento do hit, vocÃª:");
      this.rawLines.add("  1. Reduz a distÃ¢ncia que Ã© jogado para trÃ¡s.");
      this.rawLines.add("  2. MantÃ©m melhor o seu \"momentum\" (velocidade) para continuar combando.");
      this.rawLines.add("  3. Tem mais chance de cair atrÃ¡s do oponente ou em uma posiÃ§Ã£o vantajosa.");
      this.rawLines.add("  - Detalhe importante: Ele sÃ³ ativa se vocÃª estiver segurando o botÃ£o de atacar ou estiver em combate, para nÃ£o ficar pulando aleatoriamente quando tomar dano de outras coisas (como queda ou fogo) que nÃ£o sejam PvP.");
      this.rawLines.add("");
      this.rawLines.add("Â§fLegitBridge:");
      this.rawLines.add("  Simula cliques humanos (Drag Click / Butterfly) para construir pontes com seguranÃ§a (GodBridge/Breezily).");
      this.rawLines.add("  - Como funciona:");
      this.rawLines.add("    1. Segure um bloco na mÃ£o.");
      this.rawLines.add("    2. Ative o mÃ³dulo (pode ser configurado para \"Segurar tecla\" ou \"Apertar tecla\" nas configuraÃ§Ãµes).");
      this.rawLines.add("    3. Simplesmente ande para trÃ¡s mirando na borda do bloco. O mÃ³dulo farÃ¡ o \"timing\" perfeito dos cliques para vocÃª.");
      this.rawLines.add("  - Vantagens Competitivas:");
      this.rawLines.add("    - GodBridge/Breezily Sem EsforÃ§o: Permite fazer as pontes mais difÃ\u00adceis do jogo sem precisar saber fazer Drag Click ou ter 20 CPS.");
      this.rawLines.add("    - IndetectÃ¡vel (Humanizado): Diferente de macros comuns, ele varia os intervalos dos cliques e simula falhas humanas propositais para burlar Anti-Cheats.");
      this.rawLines.add("    - Salva-Vidas (Auto-Clutch): Se vocÃª errar o passo e cair, o \"Modo PÃ¢nico\" ativa instantaneamente, clicando freneticamente para colocar um bloco embaixo de vocÃª e te salvar da queda.");
      this.rawLines.add("");
      this.rawLines.add("Â§a[Visual]");
      this.rawLines.add("Â§fEncontraBau:");
      this.rawLines.add("  Destaca BaÃºs atravÃ©s das paredes, deixando o bÃ¡u brilhante e com um feixe de luz.");
      this.rawLines.add("  - Vantagem: Essencial em HG/SkyWars para localizar itens rapidamente e nÃ£o perder tempo procurando baÃºs escondidos.");
      this.rawLines.add("");
      this.rawLines.add("Â§fEncontraBigorna:");
      this.rawLines.add("  Destaca Bigornas atravÃ©s dos blocos, deixando a bigorna brilhante e com um feixe de luz.");
      this.rawLines.add("  - Vantagem: Essencial em HG/Minimush/Scrim/FlameLeague para localizar bigornas rapidamente e fazer a progredir um item seu, como uma espada, juntando duas Espadas com Afiada 1 e transformando em Afiada 2.");
      this.rawLines.add("");
      this.rawLines.add("Â§fEncontraCama:");
      this.rawLines.add("  Destaca camas atravÃ©s dos blocos, deixando a bigorna brilhante e com um feixe de luz.");
      this.rawLines.add("  - Vantagem: Fundamental em BedWars para localizar a cama inimiga instantaneamente, mesmo se estiver coberta por blocos (lÃ£, madeira, endstone), permitindo planejar o rush perfeito ou destruir a cama sem precisar procurar.");
      this.rawLines.add("");
      this.rawLines.add("Â§fAchaFerro:");
      this.rawLines.add("  Destaca minÃ©rios de ferro atravÃ©s dos blocos.");
      this.rawLines.add("  SÃ³ de passar andando no mapa e olhar para baixo aparece onde tem ferro.");
      this.rawLines.add("  - Vantagem: Essencial em cenÃ¡rios de HG, Eventos, Survival para encontrar ferro rapidamente no inÃ\u00adcio da partida, garantindo armadura e ferramentas antes dos oponentes sem perder tempo minerando Ã s cegas.");
      this.rawLines.add("");
      this.rawLines.add("Â§fEscalaCustomizada:");
      this.rawLines.add("  Permite mudar a escala apenas do seu inventÃ¡rio, baÃºs, npcs do Bedwars...");
      this.rawLines.add("  - Por que usar?");
      this.rawLines.add("    Muitos jogadores preferem usar a \"GUI Scale: Large\" ou \"Auto\" para ver o inventÃ¡rio maior e clicar mais facilmente, mas isso deixa tudo gigante (hotbar, scoreboard e etc), ocupando a tela toda.");
      this.rawLines.add("    Com este mÃ³dulo, vocÃª pode deixar o jogo em \"Small\" e o inventÃ¡rio em \"Large\", \"Normal\" ou \"Auto\"...");
      this.rawLines.add("  - Vantagem Competitiva (Refill/Recraft):");
      this.rawLines.add("    Ao deixar o inventÃ¡rio maior ou menor, vocÃª consegue ser mais preciso ao manipular itens no inventÃ¡rio ou craftar.");
      this.rawLines.add("");
      this.rawLines.add("Â§fEscolheNick (NameProtect):");
      this.rawLines.add("  Altera nicks visualmente.");
      this.rawLines.add("  Muito Usados para disfarÃ§ar nicks ou atÃ© ir PvP de brincadeira com o seu YouTuber preferido, colocando nick dele em quem vocÃª quiser.");
      this.rawLines.add("  ApÃ³s configurar o nick, Ã© orientado no chato relog para ser efetivada a alteraÃ§Ã£o por completo (No jogador real, Chat, Tab, InventÃ¡rios)");
      this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
      this.rawLines.add("  - Nick Real / Novo Nick: Digite para alterar.");
      this.rawLines.add("  - BotÃ£o de Aplicar e BotÃ£o para Resetar");
      this.rawLines.add("");
      this.rawLines.add("Â§fUltraF5:");
      this.rawLines.add("  Libera a cÃ¢mera em terceira pessoa (F5) para ir muito mais longe do que o limite padrÃ£o do Minecraft.");
      this.rawLines.add("  - Vantagem TÃ¡tica (Vision):");
      this.rawLines.add("    Permite que vocÃª afaste a cÃ¢mera para ter uma visÃ£o aÃ©rea do mapa ao seu redor, funcionando como um \"DRONE\".");
      this.rawLines.add("    - HG/SkyWars: Veja se tem inimigos indo atÃ© vocÃª, escondidos atrÃ¡s de paredes, em cima de Ã¡rvores, dentro de buracos sem precisar se expor.");
      this.rawLines.add("    - Factions/HCF: Verifique se hÃ¡ bases ou armadilhas por perto olhando por cima de muros altos.");
      this.rawLines.add("    - Minerar: Veja se tem lava ou cavernas perigosas ao seu redor antes de quebrar blocos.");
      this.rawLines.add("");
      this.rawLines.add("Â§a[Utilidades]");
      this.rawLines.add("Â§fDiamondCollector:");
      this.rawLines.add("  Coleta itens de diamante automaticamente de baÃºs assim que vocÃª os abre.");
      this.rawLines.add("  Abra o baÃº e saia correndo; se tiver diamante, jÃ¡ estarÃ¡ no seu inventÃ¡rio.");
      this.rawLines.add("  - Vantagem: Em uma partida, vocÃª nÃ£o perde tempo clicando.");
      this.rawLines.add("");
      this.rawLines.add("Â§fTankoSIM:");
      this.rawLines.add("  Simula um jogador da classe \"Tank\" (comum em HCF) gerenciando seu inventÃ¡rio.");
      this.rawLines.add("  - Funcionalidade: Joga fora automaticamente itens inÃºteis para PvP (como pÃ¡s de madeira, tesouras) ou armaduras dentro do inventÃ¡rio (NÃ£o joga as equipadas), mantendo seu inventÃ¡rio limpo para recraft e refil.");
      this.rawLines.add("  - Vantagem: Evita inventÃ¡rio cheio de lixo no meio do combate.");
      this.rawLines.add("  - CenÃ¡rio real: VocÃª pode dropar toda a sua armadura do seu inventÃ¡rio em um buraco fechado contra um inimigo e ocupar o espaÃ§o livre do inventÃ¡rio dele e vocÃª o matar facilmente, ou pode resistir a traps com pÃ¡s e tesouras que te impediriam de manipular o seu inventÃ¡rio por ocupar espaÃ§o demais nele.");
      this.rawLines.add("");
      this.rawLines.add("Â§fBotPvP:");
      this.rawLines.add("  Cria um NPC (Bot) inteligente para treino de PvP offline.");
      this.rawLines.add("  - CaracterÃ\u00adsticas: O Bot possui W-Tap, strafe e mira, simulando um jogador real.");
      this.rawLines.add("  Â§7ConfiguraÃ§Ãµes:");
      this.rawLines.add("  - Dificuldade: FÃ¡cil, MÃ©dio, DifÃ\u00adcil ou Cheater.");
      this.rawLines.add("  - Dano: Ajuste quantos coraÃ§Ãµes o bot tira por hit.");
      this.rawLines.add("  - Vantagem: Treine sua mira, combos e reduÃ§aÃµ de KB, alÃ©m disso vocÃª pode spawnar diversos bots e treinar contra vÃ¡rios oponentes.");
      this.rawLines.add("      - Dica: Combine com o mod sopa fÃ¡cil e simule um pvp no HG e evolua cada vez mais.");
      this.rawLines.add("");
      this.rawLines.add("Â§fFPSTurbo:");
      this.rawLines.add("  Otimizador extremo, configura tudo para maximizar o FPS.");
      this.rawLines.add("  - Vantagem: Essencial para rodar o Minecraft liso em PCs fracos ou melhorar o que jÃ¡ estÃ¡ bom.");
      this.rawLines.add("");
      this.rawLines.add("Â§fDespertador:");
      this.rawLines.add("  Exibe lembretes visuais na tela em horÃ¡rios programados, juntamente a um som de notificaÃ§Ã£o.");
      this.rawLines.add("  - Uso: \"Ir para o evento Ã s 19:00\", \"Tomar remÃ©dio\", \"Sair do PC\".");
      this.rawLines.add("  Nunca mais perca um evento do servidor por estar distraÃ\u00addo.");
      this.rawLines.add("");
      this.rawLines.add("Â§a[Client]");
      this.rawLines.add("Â§fMenu Principal:");
      this.rawLines.add("  O menu que vocÃª estÃ¡ usando agora.");
      this.rawLines.add("");
      this.rawLines.add("Â§fTroll:");
      this.rawLines.add("  Teste kkkk (Cuidado!).");
      this.rawLines.add("");
      this.rawLines.add("Â§7Pressione ESC para fechar.");
   }

   public void initGui() {
      super.initGui();
      this.recalculateLines();
   }

   private void recalculateLines() {
      this.displayLines.clear();
      short var1 = 280;
      Iterator var2 = this.rawLines.iterator();

      while(true) {
         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (!var3.startsWith("Â§aÂ§l") && !var3.startsWith("Â§2Â§l") && !var3.startsWith("Â§a[")) {
               if (var3.isEmpty()) {
                  this.displayLines.add("");
               } else {
                  List var4 = this.mc.fontRendererObj.listFormattedStringToWidth(var3, var1);
                  this.displayLines.addAll(var4);
               }
            } else {
               this.displayLines.add(var3);
            }
         }

         return;
      }
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.drawDefaultBackground();
      Gui.drawRect(this.width / 2 - 180, 20, this.width / 2 + 180, this.height - 20, -15658735);
      Gui.drawRect(this.width / 2 - 178, 22, this.width / 2 + 178, this.height - 22, -14540254);
      int var4 = this.displayLines.size() * 12;
      int var5 = this.height - 44;
      this.maxScroll = Math.max(0, var4 - var5);
      if (this.scroll < 0) {
         this.scroll = 0;
      }

      if (this.scroll > this.maxScroll) {
         this.scroll = this.maxScroll;
      }

      int var6 = 30 - this.scroll;
      int var7 = this.height - 25;

      for(Iterator var8 = this.displayLines.iterator(); var8.hasNext(); var6 += 12) {
         String var9 = (String)var8.next();
         if (var6 > 25 && var6 < var7) {
            if (!var9.startsWith("Â§aÂ§l") && !var9.startsWith("Â§2Â§l")) {
               if (var9.startsWith("Â§a[")) {
                  this.mc.fontRendererObj.drawStringWithShadow(var9, (float)(this.width / 2 - 170), (float)var6, -16711936);
               } else if (var9.startsWith("Â§f")) {
                  this.mc.fontRendererObj.drawStringWithShadow(var9, (float)(this.width / 2 - 170), (float)var6, -1);
               } else {
                  this.mc.fontRendererObj.drawStringWithShadow(var9, (float)(this.width / 2 - 160), (float)var6, -5592406);
               }
            } else {
               this.mc.fontRendererObj.drawStringWithShadow(var9, (float)(this.width / 2 - this.mc.fontRendererObj.getStringWidth(var9) / 2), (float)var6, -16711936);
            }
         }
      }

      if (this.maxScroll > 0) {
         int var10 = var5 * var5 / var4;
         if (var10 < 10) {
            var10 = 10;
         }

         int var11 = 22 + this.scroll * (var5 - var10) / this.maxScroll;
         Gui.drawRect(this.width / 2 + 174, 22, this.width / 2 + 178, this.height - 22, -15658735);
         Gui.drawRect(this.width / 2 + 174, var11, this.width / 2 + 178, var11 + var10, -16711936);
      }

      super.drawScreen(var1, var2, var3);
   }

   public void handleMouseInput() throws IOException {
      super.handleMouseInput();
      int var1 = Mouse.getEventDWheel();
      if (var1 != 0) {
         if (var1 > 0) {
            this.scroll -= 12;
         } else {
            this.scroll += 12;
         }
      }

   }

   public boolean doesGuiPauseGame() {
      return true;
   }
}
