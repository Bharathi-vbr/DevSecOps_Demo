
# Kubernetes Deployment for Calculator App

This directory contains Kubernetes manifests for deploying the Calculator application.

## Components

1. **Deployment** – Manages calculator pods, scaling, and rolling updates.
2. **Service** – Exposes the calculator application inside the cluster.
3. **Ingress** – Publishes the application externally using NGINX or similar.

## Prerequisites

- A running Kubernetes cluster (Minikube, k3s, EKS, GKE, AKS, etc.)
- `kubectl` configured for your cluster
- [NGINX Ingress controller](https://kubernetes.github.io/ingress-nginx/) installed if using `ingress.yaml`
- Access to the container registry (e.g., GitHub Container Registry / GHCR)

## Setup Container Registry Secret

If your image is private, create a secret before deploying:
```bash
kubectl create secret docker-registry github-container-registry
--docker-server=ghcr.io
--docker-username=YOUR_GITHUB_USERNAME
--docker-password=YOUR_GITHUB_TOKEN
--docker-email=YOUR_EMAIL
```

Update your deployment manifest to use this secret under `imagePullSecrets`.

## Deployment

Apply all manifests:
```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
kubectl apply -f ingress.yaml
```

## Accessing the Application

- **With Minikube:**
```bash
minikube service calculator-service --url
```

- **With Ingress/domain:**  
Visit the configured host (update `ingress.yaml` with your real domain).

## Scaling

To scale the application:
```bash
kubectl scale deployment calculator --replicas=5
```

## Updating

After pushing a new container image and updating the manifest:
```bash
kubectl rollout restart deployment calculator
```

## Monitoring

Check status:
```bash
kubectl get deployments
kubectl get pods
kubectl get services
kubectl get ingress
```

View logs:
```bash
kubectl logs -l app=calculator
```
---

**This K8s setup makes your calculator ready for GitOps with Argo CD and production DevSecOps workflows!**
