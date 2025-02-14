# TensorZero Integration Guide

This guide explains how to use TensorZero models with TorchServe. TensorZero provides a unified gateway for AI model serving, and this integration allows you to use TensorZero models within TorchServe's serving infrastructure.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Using TensorZero Models](#using-tensorzero-models)
- [Configuration](#configuration)
- [Examples](#examples)
- [Troubleshooting](#troubleshooting)

## Prerequisites

- TorchServe installed
- TensorZero repository cloned and built
- Rust compiler installed (required for TensorZero)

## Installation

1. Add TensorZero as a submodule:

   ```bash
   git submodule add https://github.com/Celebrum/tensorzero.git third_party/tensorzero
   git submodule update --init --recursive
   ```

2. Build TensorZero:
   ```bash
   cd third_party/tensorzero
   cargo build --release
   cd ../..
   ```

## Using TensorZero Models

TorchServe provides a dedicated handler for TensorZero models. To use it:

1. In your model archive manifest, specify the handler type as "tensorzero":

   ```json
   {
     "handler": "tensorzero"
   }
   ```

2. Package your model:

   ```bash
   torch-model-archiver --model-name my_model \
                       --version 1.0 \
                       --handler tensorzero \
                       --export-path model_store
   ```

3. Start TorchServe:
   ```bash
   torchserve --start --model-store model_store --models my_model.mar
   ```

## Configuration

The TensorZero handler supports the following configuration options in config.properties:

```properties
# TensorZero specific settings
tensorzero_model_path=path/to/model
tensorzero_max_batch_size=32
tensorzero_timeout=30
```

## Examples

Here's a complete example of using a TensorZero model with TorchServe:

1. Create a model archive:

```python
from ts.torch_handler.base_handler import BaseHandler
from tensorzero import TensorZeroModel

class MyTensorZeroHandler(BaseHandler):
    def initialize(self, context):
        self.model = TensorZeroModel.load("model.tz")

    def handle(self, data, context):
        return self.model.predict(data)
```

2. Package and serve:

```bash
torch-model-archiver --model-name tzmodel \
                    --version 1.0 \
                    --handler handler.py \
                    --export-path model_store

torchserve --start --model-store model_store --models tzmodel.mar
```

3. Make inference requests:

```bash
curl -X POST "http://localhost:8080/predictions/tzmodel" \
     -H "Content-Type: application/json" \
     -d '{"input": "your input data"}'
```

## Troubleshooting

Common issues and solutions:

1. Model Loading Errors

   - Ensure TensorZero is properly built
   - Check model path is correct
   - Verify model format is compatible

2. Runtime Errors

   - Check TensorZero version compatibility
   - Ensure all dependencies are installed
   - Verify input data format matches model expectations

3. Performance Issues
   - Adjust batch size settings
   - Monitor resource usage
   - Consider using TensorZero's optimization features

## Further Reading

- [TensorZero Documentation](https://github.com/Celebrum/tensorzero)
- [TorchServe Handler Guide](handler_guide.md)
- [Model Management](management_api.md)
