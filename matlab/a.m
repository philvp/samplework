clear; clc;
pvar x1 x2

smonom = monomials([x1;x2],0:2); %A vector of monomials in x1,x2 up to quadratic monomials
Vmonom=monomials([x1;x2],1:4);   %A vector of monomials in x1,x2 up to 4th-order monomials (excludes constant offset)

N_iter=100; %number of iterations

x1dot = -x2;     %% Van der Pol oscillator dynamics
x2dot = x1 - x2 + x2^3;     %% Van der Pol oscillator dynamics
 
p = x1^2 + x2^2;         %% A positive definite, convex function of x1,x2
eps = 1e-7;
l = eps*(x1^2 + x2^2);         %% A (preferably small) positive definite function

V = 1/2 * (x1^2 + x2^2); %% initialize V with a known Lyapunov function for the van der Pol oscillator dynamics

% pcontain(g1, g2, smonom): {g2 <= gamma} is subset of {g1 <= 0}

for iter=1:N_iter
    disp('=================')
    iter
    %%% Problem (a)
    Vdot = jacobian(V, [x1 x2]) * [x1dot; x2dot]; % Use the JACOBIAN command to obtain a function for Vdot, the time derivative of V
    [gammabnds,s1] = pcontain(Vdot,V,smonom); % Replace ?? with appropriate functions. PCONTAIN returns the bounds gammabnds and polynomial s1
    gamma_star=gammabnds(1);                 % We need the lower approximation of gamma
    
    
    %%% Problem (b)
    pvar beta;
    [bounds,s2]=pcontain(V - gamma_star, p, smonom);    % Replace ?? with appropriate functions. PCONTAIN returns the polynomial s2 (we don't need the value of beta)
    beta_star = bounds(1);
    
    if ((iter > 1) && (abs(beta_star - last_beta_star) < 1e-3))
        break;
    end
    last_beta_star = beta_star;
    last_beta_star
    
    %%% Problem (c)
    pvar beta;
    V = polydecvar('A',Vmonom,'vec'); % initialize a polynomial variable V
    Vdot = jacobian(V, [x1 x2]) * [x1dot; x2dot]; % Use JACOBIAN to obtain the polynomial variable representing the time derivative of V
    SOScons=[beta;  %% beta must be positive
             V-l;   %% V must be positive definite
             -((gamma_star - V) * s1 + Vdot + l);    %% Vdot must be negative in the region where (gamma_star-V) is positive (Replace ?? with appropriate SOS constraint)
             -((beta - p) * s2 + (V - gamma_star))];    %% The region where beta-p(x) is positive must be contained in the region where (gamma_star-V) is positive

    [info,dopt,sossol] = sosopt(SOScons,[x1;x2],-beta); % Solve the SOS problem
    beta_B=double(subs(beta,dopt)); %convert beta to a double
    V=subs(V,dopt);                 %extract the Lyapunov function V        
end
%
pcontour(V-gamma_star,0,[-2,2,-2,2],'b'); %plot the countour V-gamma_star==0
grid on

%% Plotting
% Actual Region of Attraction
[T, X] = ode45(@(t, x) (-[-x(2); x(1) - x(2) + x(2)^3]), [0; 1000], [-2 2]);
figure()
plot(X(:, 1), X(:, 2))
hold on
% Estimated region of Attraction
load('1_iteration.mat')
pcontour(V-gamma_star,0,[-2,2,-2,2],'k:+'); %plot the countour V-gamma_star==0
load('7_iterations.mat')
pcontour(V-gamma_star,0,[-2,2,-2,2],'r'); %plot the countour V-gamma_star==0

%%
load('7_iterations.mat')
V
gamma_star
beta_star
