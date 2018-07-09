%%
clear; clc;
time_span = [0 2];
initial_condition = [1 0];
[T, X] = ode45(@(t, x) mathieu(t, x), time_span, initial_condition);
plot(T,X(:,1),'-');
title('Plot of x as a function of time. Delta = -30 and Epsilon = 1');
xlabel('Time'); ylabel('x(t)');
grid on;

%%
ft = linspace(0,5,25); % Generate t for f
f = ft.^2 - ft - 3; % Generate f(t)
gt = linspace(1,6,25); % Generate t for g
g = 3*sin(gt-0.25); % Generate g(t)

Tspan = [1 5]; % Solve from t=1 to t=5
IC = 1; % y(t=0) = 1
[T Y] = ode45(@(t,y) myode(t,y,ft,f,gt,g),Tspan,IC); % Solve ODE

plot(T, Y);
title('Plot of y as a function of time');
xlabel('Time'); ylabel('Y(t)');

%%
options = odeset('RelTol',1e-4,'AbsTol',[1e-4 1e-4 1e-5]);
[T,Y] = ode45(@rigid,[0 12],[0 1 1],options);

plot(T,Y(:,1),'-',T,Y(:,2),'-.',T,Y(:,3),'.')

%%
syms x
f = x^4 - x^2
ezplot(f, [-1, 1])

%%
A = [0 -1; 1 -1]

%%
clear; clc;
syms x1 x2
dx1 = -x2
dx2 = x1 - x2 + x2^3
V = 1/2 * (3 * x1^2 + 2 * x2^2) - x1 * x2
V_dot = 3 * x1 * dx1 + 2 * x2 * dx2 - (x1 * dx2 + x2 * dx1)
V_dot = simplify(V_dot)
% ezsurfc(V_dot)
c = 0.5
ezplot(V - c)
hold on
ezplot(V_dot)
grid on

% plot( plot::Implicit2d( V_dot=20, x1=-5 .. 5, x2=-5 .. 5 ) )

%%
clear; clc;
syms x1 x2
dx1 = -x2
dx2 = x1 - x2 + x2^3
V = 1/2 * x1^2 + 1/2 * x2^2
V_dot = x1 * dx1 + x2 * dx2;
c = 0.5
ezplot(V - c)
hold on
ezplot(V_dot)
grid on