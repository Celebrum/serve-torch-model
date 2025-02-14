#include "handler_factory.hh"
#include "torch_scripted_handler.hh"
#include "tensorzero_handler.hh"

namespace torchserve
{

  std::unique_ptr<BaseHandler> HandlerFactory::create_handler(const std::string &handler_type)
  {
    if (handler_type == "tensorzero")
    {
      return std::make_unique<TensorZeroHandler>();
    }
    else if (handler_type == "torch_script")
    {
      return std::make_unique<TorchScriptedHandler>();
    }
    throw std::runtime_error("Unknown handler type: " + handler_type);
  }