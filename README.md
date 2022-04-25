![alt text](https://github.com/brunomoraisti/tonorastro/blob/master/app/src/main/res/mipmap-mdpi/logo_nome_amarelo.png?raw=true)
### Aplicativo para rastreamento de encomendas

## Link Google Play
https://play.google.com/store/apps/details?id=com.bmorais.tonorastro&hl=pt_BR&gl=US

## Contexto do problema
Em tempos e compras on-line o rastreamento de encomendas vai ficando cada vez mais difícil acompanhar, principalmente se for concentrar todos os rastreamentos em um só local. E ainda ser avisado quando houver novos andamentos.

## Solução
Desenvolvimento de um aplicativo para armazenar de forma off-line os andamentos das encomendas. Para utilizar o usuário deve informa o código de rastreamento, o app busca via API os andamentos da encomenda e armazena no banco de dados local. De acordo com o tempo escolhido o app busca de forma automática se possui novos andamentos, se tiver algum andamento novo, emiti uma notificação informando que teve novo andamento para o pedido específico. Caso não tenha internet ou a encomenda não tenha sido lançada ainda a encomenda ficará aguardando o lançamento.

## Dificuldades encontradas
- Em alguns dispositivos o android possui a otimização de bateria o que muitas vezes restringe as atualizações em segundo plano, sendo necessário alterar nas configurações do app.
- No momento o app só está rastreando encomendas postadas nos correios

## Tecnologias utilizadas
- Android Nativo (Java)
- Banco de dados local (SQLite)
- Retrofit (Http)
- Worke (Execução em segundo plano)
- Lottie (Animação de imagens)
- Firebase

## Estrutura
    tonorastro
    ├── adapter/
    ├── dao/
    ├── firebase/
    ├── http/
    ├── lib/
    ├── model/
    ├── pages/
    ├── service/
    ├── src/
    ├── AndamentoObjetoActivity
    ├── PrincipalActivity
    └── SplashActivity

## API utilizado
Desenvolvi toda a parte back-end utilizando a linguagem PHP com o framework Slim 4 com arquitetura REST e autenticação JWT. Modelo utilizado https://github.com/brunomoraisti/slim4-framework

## Telas
<img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img3.jpeg?raw=true" alt="J" width="250"/><img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img4.jpeg?raw=true" alt="J" width="250"/>
#
<img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img1.jpeg?raw=true" alt="J" width="250"/><img src="https://github.com/brunomoraisti/tonorastro/blob/master/img/img2.jpeg?raw=true" alt="J" width="250"/>


