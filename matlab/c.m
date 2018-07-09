A = [-0.6 0.8 0.5; -0.1 1.5 -1.1; 1.1 0.4 -0.2];
S{1} = [0.74 -0.21 -0.64];
S{2} = [0.37 0.86 0.37];
S{3} = [0 0 1];
Q = eye(3);
R = 0.1^2;
init_V = eye(3);
init_x = zeros(3, 1);
T = 50;

%% Part a
C = S{3};
mu = init_x;
sigma = init_V;
plot_data = [];
for t = 1 : T*100
    K = sigma*C'*pinv(C*sigma*C' + R);
    sigma = (eye(size(sigma))-K*C)*sigma;
    plot_data = [plot_data; trace(sigma)];
    
    sigma = A*sigma*A' + Q;
end

figure;
plot(plot_data(length(plot_data)) * ones(1, T), 'r--'); hold on; 
legend('Steady value', 'location', 'SouthEast');
plot(plot_data(1:T));
xlabel('Time t'); ylabel('Trace(\Sigma_{t | 0:t})');
changefont(16);
title('Using S_3');
saveas(gcf, '4a3.pdf');

%% Part b: Round robin
mu = init_x;
sigma = init_V;
plot_data = [];
robin = 1;
for t = 1 : T*100
    C = S{robin};
    K = sigma*C'*pinv(C*sigma*C' + R);
    sigma = (eye(size(sigma))-K*C)*sigma;
    plot_data = [plot_data; trace(sigma)];
    
    sigma = A*sigma*A' + Q;
    robin = robin + 1;
    if (robin > 3)
        robin = 1;
    end
end

plot(plot_data(1:T));
xlabel('Time t'); ylabel('Trace(\Sigma_{t | 0:t})');
changefont(16);
title('Round robin');
saveas(gcf, '4b.pdf');

%% Part c: Greedy selection (the best sequence: 2, 2, 1 and repeat that)
mu = init_x;
sigma = init_V;
plot_data = [];
plot_robin = [];
for t = 1 : T*100
    best_robin = -1;
    best_trace = 1e18;
    for robin = 1 : length(S)
        C = S{robin};
        K = sigma*C'*pinv(C*sigma*C' + R);
        cur = (eye(size(sigma))-K*C)*sigma;        
        if (trace(cur) < best_trace)
            best_trace = trace(cur);
            best_robin = robin;
        end
    end
    
    C = S{best_robin};
    K = sigma*C'*pinv(C*sigma*C' + R);
    sigma = (eye(size(sigma))-K*C)*sigma;
    plot_data = [plot_data; trace(sigma)];
    plot_robin = [plot_robin; best_robin];
    
    sigma = A*sigma*A' + Q;
end

plot(plot_data(1:T));
xlabel('Time t'); ylabel('Trace(\Sigma_{t | 0:t})');
changefont(16);
title('Greedy selection. The optimal sequence is (2, 2, 1) and repeat');
saveas(gcf, '4c.pdf');
